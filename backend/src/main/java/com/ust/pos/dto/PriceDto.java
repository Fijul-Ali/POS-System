package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PriceDto extends CommonDto {
    private String product;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private BigDecimal mrp;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
