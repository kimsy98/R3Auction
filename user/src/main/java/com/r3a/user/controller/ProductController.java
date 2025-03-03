package com.r3a.user.controller;

import com.r3a.user.dto.ProductCreateDto;
import com.r3a.user.entity.Product;
import com.r3a.user.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProductController {
    private final ProductImageService productImageService;
    private final ModelMapper modelMapper;
//    private final ProductService productService;//깃 pull로 받아오기 createProduct 메서드에서 uploadProductImage 메서드 사용해서 이미지 저장

    @PostMapping("/products/new")             //@Valid  사용위해 디펜던시 추가하고 이 어노테이션으로 dto 유효성 검사 하자
    public ResponseEntity<String> createProduct(            @RequestParam("images") List<MultipartFile> images, @ModelAttribute ProductCreateDto requestDto) {
//        Long productId = productService.createProduct(requestDto, images); // 저장한 상품의 pk
//        public Long createProduct(ProductCreateDto requestDto, List<MultipartFile> images) {
            if (requestDto.getStartPrice() < 0) {
                throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
            }
            if (images.size()==0){
                throw new IllegalArgumentException("사진은 최소 1장 이상이어야 합니다.");
            }
            // DTO를 엔티티로 매핑
            Product product = modelMapper.map(requestDto, Product.class);
//            productRepositoryV1.save(product);

            // 추가 - 썸네일 저장 메서드 실행
            productImageService.uploadProductImage(product, images);
//            return product.getProductId();
//        }



        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록 완료. Id : ");
    }


}
