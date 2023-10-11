package com.example;

import com.dto.PaymentDto;
import com.entity.Account;
import com.entity.Payment;
import com.repository.AccountRepository;
import com.repository.PaymentRepository;
import com.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;


@TestPropertySource(properties = {
        "payment.system.url = http://localhost:9000/payment",
})
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @Value("${payment.system.url}")
    private String thirdPartyPaymentUrl;

    @BeforeEach
    public void setup() {
        paymentService.setRestTemplate(restTemplate);
    }

    @Test
    public void testProcessOnlinePayment_Successful() throws Exception {
        ResponseEntity<String> successfulResponse = ResponseEntity.ok("Payment validated successfully");

        when(restTemplate.exchange(eq(thirdPartyPaymentUrl), Mockito.eq(HttpMethod.POST), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(successfulResponse);

        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());
        when(accountRepository.getById(anyLong())).thenReturn(createAccount());

        paymentService.processOnlinePayment(createPaymentDto());

        verify(paymentRepository).save(any(Payment.class));
        verify(accountRepository).save(any(Account.class));
    }


    private PaymentDto createPaymentDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPayment_id("123");
        paymentDto.setPayment_type("ONLINE");
        paymentDto.setAmount(BigDecimal.TEN);
        paymentDto.setAccount_id("10");
        paymentDto.setCredit_card("1234567890123456");
        return paymentDto;
    }
    private Account createAccount() {
        Account account = new Account();
        account.setAccountId(10L);
        account.setName("Mahdieh");
        return account;
    }
}
