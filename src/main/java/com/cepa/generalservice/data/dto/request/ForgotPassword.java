package com.cepa.generalservice.data.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ForgotPassword {
    public String email;
    public String password;
    public String confirmPassword;
}
