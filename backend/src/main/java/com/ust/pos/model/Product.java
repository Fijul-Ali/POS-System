package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "products")
public class Product extends CommonFields {
    private List<String> categories;
    private String unit;
    private String brand;
    private String model;
}
