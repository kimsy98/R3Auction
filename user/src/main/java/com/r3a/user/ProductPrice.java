package com.r3a.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ProductPrice {
    private int StartPrice;
    private int SellPrice;

}
