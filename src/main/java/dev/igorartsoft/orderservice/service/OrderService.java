package dev.igorartsoft.orderservice.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.igorartsoft.orderservice.dto.OrderRequest;
import dev.igorartsoft.orderservice.event.OrderEvent;
import dev.igorartsoft.orderservice.model.OrderDocument;
import dev.igorartsoft.orderservice.repository.OrderRepository;

@Service
public class OrderService {

	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
	
    private final OrderRepository orderRepository;
    private final OrderProducerService orderProducerService;

    public OrderService(OrderRepository orderRepository,
                        OrderProducerService orderProducerService) {
        this.orderRepository = orderRepository;
        this.orderProducerService = orderProducerService;
    }

    public boolean createOrder(OrderRequest request) {

        if (orderRepository.existsByOrderId(request.orderId())) {
        	log.debug( "Order with this orderId already exist" );
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
        log.debug("Order created " + order.getOrderId() );
        OrderEvent event = new OrderEvent(
                UUID.randomUUID().toString(),
                request.orderId(),
                request.customerId(),
                request.amount(),
                now
        );

        orderProducerService.sendOrderEvent(event);
        log.debug("Kafka event has been created " + event.eventId() );
        return true;
    }

	public Optional<OrderDocument> getOrder(String orderId) {
		
		Optional<OrderDocument> orderRequestInfo = orderRepository.findByOrderId(orderId);
		return orderRequestInfo;
		
	}
}