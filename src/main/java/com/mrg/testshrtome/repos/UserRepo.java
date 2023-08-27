package com.mrg.testshrtome.repos;

import com.mrg.testshrtome.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends BaseRepo<UserEntity>{
    Optional<UserEntity> findByUserName(String userName);
}
