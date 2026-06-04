package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "carts")
@Getter
@Setter
public class Cart extends CommonFields{
    private BigDecimal originalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalPrice;
    private String coupon;
}
