package com.example.DCMS.DTOs;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class binDTO {
    private String binValue;
    private boolean checkSIExternal;
    private String billingCurrency;
    private boolean status;
    private String binType;

}
