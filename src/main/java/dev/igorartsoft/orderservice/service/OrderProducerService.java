package dev.igorartsoft.orderservice.service;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.igorartsoft.orderservice.event.OrderEvent;

@Service
public class OrderProducerService {

    private static final String TOPIC_NAME = "orders";

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderEvent orderEvent) {
        kafkaTemplate.send(TOPIC_NAME, orderEvent.orderId(), orderEvent);
    }
}