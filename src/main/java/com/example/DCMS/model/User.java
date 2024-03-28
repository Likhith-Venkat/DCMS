package com.example.DCMS.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Setter
@Getter
@Document(collection="Users")
public class User {
    @Id
    private String Username;
    private String Email;
    private String Password;
    private String Type;
    private String MC_type;
    private String Access;
}
