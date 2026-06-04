package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "orders")
@Getter
@Setter
public class Order extends CommonFields {
    private String cart;
    private String customer;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal originalAmount;
    private String orderStatus; // PENDING, COMPLETED, CANCELLED
    private LocalDateTime placedAt;
}