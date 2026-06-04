package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cartEntries")
@Getter
@Setter
public class CartEntry extends CommonFields {
    private String product;
    private BigDecimal quantity = new BigDecimal(0);
    private String coupon;
    private BigDecimal mrp;
    private BigDecimal price;
    private BigDecimal costPrice;
    private BigDecimal totalPrice;
    private String cart;
    private BigDecimal discount;
    private BigDecimal totalMrp;
}
