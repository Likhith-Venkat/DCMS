package com.example.DCMS.DTOs;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApproveDTO
{
    String url;
    String method;
    String id;
}
