package com.example.DCMS.DTO;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinDTO {
    private String binValue;
    private boolean checkSIExternal;
    private String billingCurrency;
    private boolean status;
    private String binType;
}
