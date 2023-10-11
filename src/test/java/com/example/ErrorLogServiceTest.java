package com.example;

import com.dto.PaymentDto;
import com.service.ErrorLogService;
import com.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class ErrorLogServiceTest {
    @Mock
    private ErrorLogService errorLogService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testProcessOnlinePayment_NetworkError() throws Exception {
        PaymentDto paymentDto = new PaymentDto(); // Create a valid PaymentDto
        ResponseEntity<String> errorResponse = ResponseEntity.status(500).body("Internal Server Error");

        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(errorResponse);

        paymentService.processOnlinePayment(paymentDto);

        verify(errorLogService).logError(any());
    }

    @Test
    void testProcessOnlinePayment_Exception() throws Exception {
        PaymentDto paymentDto = new PaymentDto(); // Create a valid PaymentDto
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        paymentService.processOnlinePayment(paymentDto);

        verify(errorLogService).logError(any());
    }
}
