package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "warehouses")
public class Warehouse extends CommonFields {
    private String address;
    private String contactNumber;
    
}
