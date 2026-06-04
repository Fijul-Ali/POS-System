package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
@Getter
@Setter
public class Brand extends CommonFields {
}
