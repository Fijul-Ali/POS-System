package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "racks")
@Getter
@Setter
public class Racks extends CommonFields {
    private List<String> shelves;
}
