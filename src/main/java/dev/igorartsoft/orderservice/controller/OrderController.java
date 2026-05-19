package dev.igorartsoft.orderservice.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.igorartsoft.orderservice.dto.OrderRequest;
import dev.igorartsoft.orderservice.event.OrderEvent;
import dev.igorartsoft.orderservice.service.OrderProducerService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducerService orderProducerService;

    public OrderController(OrderProducerService orderProducerService) {
        this.orderProducerService = orderProducerService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {

        OrderEvent event = new OrderEvent(
                UUID.randomUUID().toString(),
                request.orderId(),
                request.customerId(),
                request.amount(),
                Instant.now()
        );

        orderProducerService.sendOrderEvent(event);

        return ResponseEntity.ok("Order message sent to Kafka. orderId=" + request.orderId());
    }
}


