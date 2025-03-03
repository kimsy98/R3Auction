package com.r3a.user.entity;

import com.r3a.user.Period;
import com.r3a.user.ProductCategory;
import com.r3a.user.ProductPrice;
import com.r3a.user.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;
    String description;
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    private ProductCategory productCategory;

    @Embedded
    private ProductPrice productPrice;
    @Embedded
    private Period period;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();


    public Product(Long productId){
        this.productId = productId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
