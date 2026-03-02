package com.shopsphere.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String orderId;
    private String userId;
    private List<OrderItem> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private User.Address shippingAddress;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}
