package com.example.DCMS.DTOs;


import com.example.DCMS.enums.Method;
import com.mongodb.lang.NonNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class approveDTO
{
    String url;
    Method method;
    String id;

}
