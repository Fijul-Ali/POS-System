package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Getter
@Setter
public class Category extends CommonFields {
    private String superCategory;
}
