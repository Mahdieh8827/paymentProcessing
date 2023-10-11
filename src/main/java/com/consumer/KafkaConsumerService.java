package com.consumer;

import com.dto.PaymentDto;
import com.service.PaymentService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final PaymentService paymentService;

    public KafkaConsumerService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.topicOnline}")
    public void consumeOnlinePayment(ConsumerRecord<String, PaymentDto> onlinePayment) throws Exception {
        PaymentDto payment = onlinePayment.value();
        paymentService.processOnlinePayment(payment);
    }

    @KafkaListener(topics = "${spring.kafka.topic.topicOffline}")
    public void consumeOfflinePayment(ConsumerRecord<String, PaymentDto> offlinePayment) {
        PaymentDto payment = offlinePayment.value();
        paymentService.processOfflinePayment(payment);
    }
}

