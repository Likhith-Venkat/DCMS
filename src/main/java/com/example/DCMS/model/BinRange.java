package com.example.DCMS.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Bin_range")
public class BinRange {
    private Bin bin;
    @Id
    private String bin_Range_Name;
    private Integer product_Code;
    private Integer from_Card_Number;
    private Integer to_Card_Number;
    private String network_Type;
}
