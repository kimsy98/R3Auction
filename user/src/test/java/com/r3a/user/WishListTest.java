//package com.r3a.user;
//
//import com.r3a.user.entity.Product;
//import com.r3a.user.entity.UserEntity;
//import com.r3a.user.entity.Wish;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class WishListTest {
////    @Test
////    public void WishList생성자테스트(){
////        WishList wishList = new WishList();
////        assertThat(wishList).isEqualTo(new WishList());
////    }
//    @Test
//    public void Product생성자테스트(){
//        Product product = new Product(1L);
//        assertThat(product).isEqualTo(new Product(1L));
//    }
//
//    @Test
//    public void User생성자테스트(){
//        UserEntity user = new UserEntity(1L);
//        assertThat(user).isEqualTo(new UserEntity(1L));
//    }
//
//    @Test
//    public void Wish생성자테스트(){
//        UserEntity user =new UserEntity(1L);
//        Product product  = new Product(1L);
//        Wish wish = new Wish(user, product);
//        assertThat(wish).isEqualTo(new Wish(new UserEntity(1L), new Product(1L)));
//    }
////    @Test
////    public void WishList추가(){
////        UserEntity user =new UserEntity(1L);
////        Product product  = new Product(1L);
////        Wish wish = new Wish(user, product);
////
////        WishList wishList = new WishList();
////        wishList.addWish(wish);
////        assertThat(wishList.itemList.get(0)).isEqualTo(wish);
////    }
//
////    @Test
////    public void WishList삭제(){
////        UserEntity user =new UserEntity(1L);
////        Product product  = new Product(1L);
////        Wish wish = new Wish(user, product);
////
////        WishList wishList = new WishList();
////        wishList.addWish(wish);
////        wishList.deletWish(wish);
////
////        assertThat(wishList.itemList.size()).isEqualTo(0);
////    }
//
//    @Test
//    public void WishList조회(){
//        WishList wishList = new WishList(new ArrayList<Wish>());
//
//        UserEntity user =new UserEntity(1L);
//        Product product  = new Product(1L);
//        Long userId = 1L;
//        Long itemId =1L;
//        Wish wish = new Wish(user, product);
//
//        wishList.addWish(wish);
//        assertThat(wishList.itemList.get(0)).isEqualTo(wish);
//    }
//    @Test
//    public void 찜목록에있는지검증(){
//
//    }
//
//}
