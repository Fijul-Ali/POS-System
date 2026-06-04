package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StockDto extends CommonDto {
    private Integer quantity;
    private LocalDateTime lastUpdated;
    private String warehouse;
    private String product;
}
