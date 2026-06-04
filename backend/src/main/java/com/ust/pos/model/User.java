package com.ust.pos.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User extends CommonFields {
    private String username;
    private String name;
    @Field("phone_no")
    private String phoneNo;
    private List<String> roles;
    private String password;
}
