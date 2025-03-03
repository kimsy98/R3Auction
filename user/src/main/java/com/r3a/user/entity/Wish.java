package com.r3a.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;

import java.util.Objects;

@Entity
//@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long WishId;

//    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Wish(UserEntity user, Product product){
        this.user = user;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wish wish)) return false;
        return Objects.equals(user, wish.user) && Objects.equals(product, wish.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }
}
