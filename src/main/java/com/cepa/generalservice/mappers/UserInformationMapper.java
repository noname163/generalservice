package com.cepa.generalservice.mappers;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.entities.UserInformation;

@Component
public class UserInformationMapper {

    public UserInformation mapDtoToEntity(UserRegister userRegister) {
        return UserInformation
                .builder()
                .email(userRegister.getEmail())
                .fullName(userRegister.getFullName())
                .role(userRegister.getRole())
                .password(userRegister.getPassword())
                .build();
    }
}
