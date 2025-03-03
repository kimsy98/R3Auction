package com.r3a.user.service;

import com.r3a.user.entity.Product;
import com.r3a.user.entity.ProductImage;
import com.r3a.user.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private static final String UPLOAD_PATH = "src/main/resources/upload/productImages/";
    public void uploadProductImage(Product product, List<MultipartFile> images){
        try {
            for (MultipartFile file : images) {
                saveImage(file);
                //save는 product 연관관계에 의해 product 추가시 같이 진행되도록
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImage(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

        String uuid = UUID.randomUUID().toString();

        String savefileName = UPLOAD_PATH + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(savefileName);
        file.transferTo(savePath);

    }
}
