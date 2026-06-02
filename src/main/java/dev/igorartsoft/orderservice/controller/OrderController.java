package dev.igorartsoft.orderservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.igorartsoft.orderservice.dto.OrderRequest;
import dev.igorartsoft.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {

    	log.debug("Request recieved by createOrder" + request.toString() );
    	
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
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderRequest> getOrder(@PathVariable String orderId) {

        return orderService.getOrder(orderId)
                .map(orderDocument -> new OrderRequest(
                        orderDocument.getOrderId(),
                        orderDocument.getCustomerId(),
                        orderDocument.getAmount()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
}



