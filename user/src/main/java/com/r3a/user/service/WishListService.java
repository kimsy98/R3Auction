package com.r3a.user.service;

import com.r3a.user.entity.Product;
import com.r3a.user.entity.UserEntity;
import com.r3a.user.entity.Wish;
import com.r3a.user.repository.ProductRepository;
import com.r3a.user.repository.UserRepository;
import com.r3a.user.repository.WishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    @Transactional
    public void addWishList(Long productId, Long userId){
//        UserEntity user = userRepository.findByUserName(userName);
//        Product product = productRepository.findById(productId).orElseThrow();
        if (wishRepository.findByUser_IdAndProduct_ProductId(userId, productId).isPresent()) {
            throw new IllegalArgumentException("Item is already in the wishlist");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // DB에서 실제 조회하지 않고 프록시 객체 생성 (효율 위해)
//        User user = entityManager.getReference(User.class, userId);
//        Product product = entityManager.getReference(Product.class, productId);

        Wish wish = new Wish(user, product);
        wishRepository.save(wish);
    }

    public void deleteWishList(Long productId,String userName) {
        UserEntity user = userRepository.findByUserName(userName);
        Long userId = user.getId();
        wishRepository.deleteByUser_IdAndProduct_ProductId(productId, userId);
    }
}
