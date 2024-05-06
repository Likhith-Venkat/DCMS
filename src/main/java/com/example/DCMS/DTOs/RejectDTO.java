package com.example.DCMS.DTOs;

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
    String id;
    @NotNull(message = "rejectReason cannot be null")
    String rejectReason;
}
