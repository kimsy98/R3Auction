    package com.r3a.user.repository;
import com.r3a.user.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
//    void deleteByRefreshToken(String refreshToken);
}
