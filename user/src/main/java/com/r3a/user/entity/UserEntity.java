package com.r3a.user.entity;

import com.r3a.user.Point;
import com.r3a.user.RatingScore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Entity
@Table
@Getter
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
public class UserEntity {
//    UserEntity(Long id){
//        this.id = id;
//    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String nickName;

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
