package com.r3a.user.repository;

import com.r3a.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

//    Optional<UserEntity> findByPoint(Long aLong);
    UserEntity findByUserName(String username);

}
