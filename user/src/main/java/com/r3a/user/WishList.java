package com.r3a.user;

import com.r3a.user.entity.Wish;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishList {

    List<Wish> itemList = new ArrayList<>();

    public WishList(List<Wish> itemList) {
        this.itemList = itemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishList wishList)) return false;
        return Objects.equals(itemList, wishList.itemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemList);
    }

    public void addWish(Wish wish) {
        if(!itemList.contains(wish))itemList.add(wish);
    }

    public void deletWish(Wish wish) {
        itemList.remove(wish);
    }
}
