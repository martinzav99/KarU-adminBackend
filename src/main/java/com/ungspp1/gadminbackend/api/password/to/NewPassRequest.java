package com.ungspp1.gadminbackend.api.password.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPassRequest {
    private String email;
    private String password;

}
