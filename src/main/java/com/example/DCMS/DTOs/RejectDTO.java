package com.example.DCMS.DTOs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RejectDTO
{
    String id;
    String rejectReason;
}
