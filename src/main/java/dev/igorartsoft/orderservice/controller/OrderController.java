package dev.igorartsoft.orderservice.controller;

import dev.igorartsoft.orderservice.service.OrderService;
import dev.igorartsoft.orderservice.dto.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {

        boolean created = orderService.createOrder(request);

        if (!created) {
            return ResponseEntity
                    .status(409)
                    .body("Order already exists. orderId=" + request.orderId());
        }

        return ResponseEntity
                .status(201)
                .body("Order created and event sent. orderId=" + request.orderId());
    }
}


