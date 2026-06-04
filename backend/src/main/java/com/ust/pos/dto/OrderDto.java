package com.ust.pos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto extends CommonDto {
    private String cart;
    private String customer;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal originalAmount;
    private String orderStatus;
    private LocalDateTime placedAt;
}