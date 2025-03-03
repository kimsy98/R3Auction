package com.r3a.user.repository;

import com.r3a.user.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
public interface WishRepository extends JpaRepository<Wish, Long> {

//    void deleteById(Long wishId);
    List<Wish> findByUser_Id(Long userId);
    Optional<Wish> findByUser_IdAndProduct_ProductId(Long userId, Long productId);

    void deleteByUser_IdAndProduct_ProductId(Long productId, Long userId);


}
