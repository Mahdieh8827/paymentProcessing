package com.service;

import com.model.ErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ErrorLogService {
    @Value("${log.system.url}")
    private String logSystemUrl;

    public void logError(ErrorLog errorLog) {
        try {
            var restTemplate = new RestTemplate();
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ErrorLog> requestEntity = new HttpEntity<>(errorLog, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(logSystemUrl , requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Error logged successfully.");
            } else {
                log.error("Error logging failed. HTTP Status: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("Error logging failed. " + e.getMessage());
        }
    }
}
