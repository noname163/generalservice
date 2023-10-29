package com.cepa.generalservice.data.object;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser {
    private String email;
    private String role;
}
