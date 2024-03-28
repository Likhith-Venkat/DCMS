package com.example.DCMS.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document(collection="Product_req")
public class Product_req {
    @Id
    private String Product_Name;
    private String Selection_Criteria;
    private String card_Type;
    private String Tier_Value;
    private String Scheme_Code;
    private String Plastic_Code;
    private String Default_Bin_Range;
    private String Allowed_Bin_Range;
    private User Maker;
    private User Checker;
    private String Reason;
}
