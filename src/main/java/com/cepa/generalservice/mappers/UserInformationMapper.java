package com.cepa.generalservice.mappers;

import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.UserRegister;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.data.entities.UserInformation;

@Component
public class UserInformationMapper {

    public UserInformation mapDtoToEntity(UserRegister userRegister) {
        return UserInformation
                .builder()
                .email(userRegister.getEmail())
                .role(userRegister.getRole())
                .fullName(userRegister.getFullName())
                .password(userRegister.getPassword())
                .build();
    }

    public UserResponse mapEntityToDto(UserInformation userInformation) {
        return UserResponse
                .builder()
                .id(userInformation.getId())
                .fullName(userInformation.getFullName())
                .email(userInformation.getEmail())
                .url(userInformation.getImageURL())
                .dateOfBirth(userInformation.getDateOfBirth())
                .createDate(userInformation.getCreateDate())
                .build();
    }

}
