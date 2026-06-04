package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartEntryDto extends CommonDto{
    private String product;
    private BigDecimal quantity;
    private String coupon;
    private BigDecimal mrp;
    private BigDecimal price; //20
    private BigDecimal totalPrice; // 20* qty - discount
    private BigDecimal costPrice; // 25 *
    private String cart;
    private BigDecimal discount;
    private BigDecimal totalMrp;
}
