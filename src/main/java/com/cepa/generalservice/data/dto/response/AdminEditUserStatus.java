package com.cepa.generalservice.data.dto.response;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cepa.generalservice.data.constants.UserStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdminEditUserStatus {
    @NotNull(message = "User id is require")
    private Long userId;
    @NotNull(message = "User status is require")
    private UserStatus userStatus;
}
