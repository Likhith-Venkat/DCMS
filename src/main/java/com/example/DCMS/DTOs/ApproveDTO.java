package com.example.DCMS.DTOs;


import com.example.DCMS.enums.Method;
import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApproveDTO
{
    @NotNull(message = "url cannot be null")
    String url;
    @Pattern(regexp = "POST|PUT|GET|DELETE", message = "Invalid method value")
    @NotNull(message = "Method cannot be null")
    Method method;
    @NotNull(message = "id cannot be null")
    String id;
}
