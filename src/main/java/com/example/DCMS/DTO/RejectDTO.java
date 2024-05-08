package com.example.DCMS.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RejectDTO
{
    @NotNull(message = "id cannot be null")
    private String id;
    @NotNull(message = "rejectReason cannot be null")
    private String rejectReason;
}
