package com.example.DCMS.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Bin")
public class Bin {
    @Id
    private String bin;
}
