package com.example.DCMS.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Product_req")
public class ProductReq {
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
    private String Status;
}
