package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "stocks")
public class Stock extends CommonFields {
    private Integer quantity;
    private String warehouse;
    private String product;
}
