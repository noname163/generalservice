package com.cepa.generalservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.UserInformation;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    
}
