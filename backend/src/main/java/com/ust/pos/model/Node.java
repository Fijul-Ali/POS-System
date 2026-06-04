package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "nodes")
@Getter
@Setter
public class Node extends CommonFields {
    private List<String> roles;
}
