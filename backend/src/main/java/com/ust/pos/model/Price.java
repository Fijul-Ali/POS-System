package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "prices")
public class Price extends CommonFields {
    private String product;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private BigDecimal mrp;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
