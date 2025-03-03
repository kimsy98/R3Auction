package com.r3a.user.entity;

import com.r3a.user.Point;
import com.r3a.user.RatingScore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table
@Getter
@Setter
//@RequiredArgsConstructor
//@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
//    UserEntity(Long id){
//        this.id = id;
//    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String nickName;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Embedded
    private Point point;

    @Embedded
    private RatingScore ratingScore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void resetTransactionCount() {
        this.ratingScore.resetTransactionCount();
    }

    public void calcScore(){
        this.ratingScore.calcScore();
    }
}
