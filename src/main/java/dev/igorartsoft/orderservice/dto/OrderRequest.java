package dev.igorartsoft.orderservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrderRequest(

        @NotBlank(message = "orderId is required")
        @Size(max = 50, message = "orderId must not exceed 50 characters")
        @Pattern(
                regexp = "^[A-Za-z0-9._:-]+$",
                message = "orderId may contain only letters, numbers, dot, underscore, colon, and hyphen"
        )
        String orderId,

        @NotBlank(message = "customerId is required")
        @Size(max = 50, message = "customerId must not exceed 50 characters")
        @Pattern(
                regexp = "^[A-Za-z0-9._:-]+$",
                message = "customerId may contain only letters, numbers, dot, underscore, colon, and hyphen"
        )
        String customerId,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than 0")
        @Digits(integer = 12, fraction = 2, message = "amount must have up to 12 digits and 2 decimal places")
        BigDecimal amount
//
//        @NotBlank(message = "currencyCode is required")
//        @Pattern(
//                regexp = "^[A-Z]{3}$",
//                message = "currencyCode must be a 3-letter uppercase currency code, for example CAD or USD"
//        )
//        String currencyCode
) {
}