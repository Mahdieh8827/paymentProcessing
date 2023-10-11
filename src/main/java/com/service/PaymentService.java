package com.service;

import com.repository.AccountRepository;
import com.repository.PaymentRepository;
import com.dto.PaymentDto;
import com.entity.Account;
import com.model.ErrorLog;
import com.entity.Payment;
import com.model.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private AccountRepository accountRepository;
    private ErrorLogService errorLogService;

    private RestTemplate restTemplate;

    @Value("${payment.system.url}")
    private String thirdPartyPaymentUrl;

    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository, ErrorLogService errorLogService) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.errorLogService = errorLogService;
        this.restTemplate = new RestTemplate();
    }

    //Just for test
    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }
    @Transactional
    public void processOnlinePayment(PaymentDto paymentDto) throws Exception {
        try {
            ResponseEntity<String> response = sendPaymentValidationRequest(paymentDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                Payment payment = convertToEntity(paymentDto);
                paymentRepository.save(payment);
                updateAccountLastPaymentDate(payment.getAccount());
            } else {
                logPaymentValidationError(paymentDto.getPayment_id(), ErrorType.NETWORK.getValue(), "Validation failed");
            }
        }
        catch(HttpServerErrorException ex)
        {
            logPaymentValidationError(paymentDto.getPayment_id(), ErrorType.NETWORK.getValue(),ex.getMessage());
        }
        catch(SQLException ex)
        {
            logPaymentValidationError(paymentDto.getPayment_id(), ErrorType.DATABASE.getValue(),ex.getMessage());
        }
        catch (Exception ex) {
            logPaymentValidationError(paymentDto.getPayment_id(), ErrorType.OTHER.getValue(),ex.getMessage());
        }
    }
    @Transactional
    public void processOfflinePayment(PaymentDto paymentDto) {
       Payment payment = convertToEntity(paymentDto);
        paymentRepository.save(payment);
        updateAccountLastPaymentDate(payment.getAccount());
    }

    private Payment convertToEntity(PaymentDto paymentDto)
    {
        Payment payment = new Payment();
        payment.setPaymentId(paymentDto.getPayment_id());
        payment.setAccount(accountRepository.getById(Long.valueOf(paymentDto.getAccount_id())));
        payment.setPaymentType(paymentDto.getPayment_type());
        payment.setCreditCard(paymentDto.getCredit_card());
        payment.setAmount(paymentDto.getAmount());
        payment.setCreatedOn((java.sql.Date) paymentDto.getCreated_on());
        return payment;
    }

    public ResponseEntity<String> sendPaymentValidationRequest(PaymentDto payment) {
        var headers = new HttpHeaders();
        Map<String, Object> requestBody = new HashMap<>();
        headers.setContentType(MediaType.APPLICATION_JSON);
        requestBody.put("payment", payment);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return this.restTemplate.exchange(thirdPartyPaymentUrl, HttpMethod.POST, requestEntity, String.class);
    }

    private void updateAccountLastPaymentDate(Account account) {
        account.setLastPaymentDate(new Date());
        accountRepository.save(account);
    }

    private void logPaymentValidationError(String paymentId,String errorType, String errorDescription) throws Exception {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setPayment_id(paymentId);
        errorLog.setError(errorType);
        errorLog.setError_description(errorDescription);
        errorLogService.logError(errorLog);
    }
}

