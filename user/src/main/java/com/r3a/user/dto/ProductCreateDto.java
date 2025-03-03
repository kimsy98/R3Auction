package com.r3a.user.dto;

// ProductListDto

// import 생략

import com.r3a.user.Period;
import com.r3a.user.ProductCategory;
import com.r3a.user.ProductPrice;
import com.r3a.user.ProductStatus;
import com.r3a.user.entity.ProductImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCreateDto {
    private Long productId;
    private String description;
    private ProductStatus productStatus;
    private ProductCategory productCategory;
    private int StartPrice;
//    private int SellPrice;
//    private LocalDateTime TransactionStartDate;
//    private LocalDateTime TransactionEndDate;
}
