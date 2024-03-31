package com.example.DCMS.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@Document(collection = "Bin_range")
public class BinRange {
    private Bin Bin;
    @Id
    private String Bin_Range_Name;
    private Integer Product_Code;
    private Integer From_Card_Number;
    private Integer To_Card_Number;
    private String Network_Type;
}
