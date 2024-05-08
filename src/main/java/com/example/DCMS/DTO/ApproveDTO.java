package com.example.DCMS.DTO;


import com.example.DCMS.enums.Method;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApproveDTO
{
    @NotNull(message = "url cannot be null")
    private String url;
    @Pattern(regexp = "POST|PUT|GET|DELETE", message = "Invalid method value")
    @NotNull(message = "Method cannot be null")
    private Method method;
    @NotNull(message = "id cannot be null")
    private String id;
}
