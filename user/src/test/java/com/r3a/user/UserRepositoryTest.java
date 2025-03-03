package com.r3a.user;


import com.r3a.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository가Null이아님(){
        assertThat(userRepository).isNull();
    }
    @Test
    public void 유저등록(){

    }
    @Test
    public void UserEntity생성자테스트(){

    }


}
