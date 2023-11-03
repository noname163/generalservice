package com.cepa.generalservice.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.entities.UserInformation;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    public Optional<UserInformation> findByEmail(String email);

    public Optional<UserInformation> findByEmailAndStatus(String email, UserStatus status);

    public Optional<UserInformation> findByIdAndStatus(Long id, UserStatus status);
}
