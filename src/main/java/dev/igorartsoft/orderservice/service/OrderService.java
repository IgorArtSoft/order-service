package dev.igorartsoft.orderservice.service;

import dev.igorartsoft.orderservice.event.OrderEvent;
import dev.igorartsoft.orderservice.model.OrderDocument;
import dev.igorartsoft.orderservice.repository.OrderRepository;
import dev.igorartsoft.orderservice.dto.OrderRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducerService orderProducerService;

    public OrderService(OrderRepository orderRepository,
                        OrderProducerService orderProducerService) {
        this.orderRepository = orderRepository;
        this.orderProducerService = orderProducerService;
    }

    public boolean createOrder(OrderRequest request) {

        if (orderRepository.existsByOrderId(request.orderId())) {
            return false;
        }

        Instant now = Instant.now();

        OrderDocument order = new OrderDocument(
                request.orderId(),
                request.customerId(),
                request.amount(),
                "CREATED",
                now
        );

        orderRepository.save(order);

        OrderEvent event = new OrderEvent(
                UUID.randomUUID().toString(),
                request.orderId(),
                request.customerId(),
                request.amount(),
                now
        );

        orderProducerService.sendOrderEvent(event);

        return true;
    }

	public Optional<OrderDocument> getOrder(String orderId) {
		
		Optional<OrderDocument> orderRequestInfo = orderRepository.findByOrderId(orderId);
		return orderRequestInfo;
		
	}
}