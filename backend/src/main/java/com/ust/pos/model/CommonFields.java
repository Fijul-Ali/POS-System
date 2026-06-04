package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class CommonFields {
    @Id
    private String id;
    private String identifier;
    private boolean status = true;
    private String description;
}
