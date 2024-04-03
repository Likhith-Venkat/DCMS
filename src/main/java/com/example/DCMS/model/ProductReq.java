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
    private String product_Name;
    private String selection_Criteria;
    private String card_Type;
    private String tier_Value;
    private String scheme_Code;
    private String plastic_Code;
    private String default_Bin_Range;
    private String allowed_Bin_Range;
    private String maker;
    private String checker;
    private String rejectReason;
    private String status;
}
