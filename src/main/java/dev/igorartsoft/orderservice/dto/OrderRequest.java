package dev.igorartsoft.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(
        String orderId,
        String customerId,
        BigDecimal amount
) {
}