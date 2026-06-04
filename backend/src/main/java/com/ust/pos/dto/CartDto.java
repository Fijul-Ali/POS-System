package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartDto extends CommonDto{
    private BigDecimal originalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalPrice;
    private String coupon;
    private List<CartEntryDto> entryDtoList;
}
