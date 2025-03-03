package com.r3a.user.controller;

import com.r3a.user.jwt.JWTUtil;
import com.r3a.user.service.WishListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;
    private final JWTUtil jwtUtil;
    @PostMapping("/wish-lists")
    public ResponseEntity<?> addWishList(@RequestBody Long productId, HttpServletRequest request){

        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("access")) {

                token = cookie.getValue();
            }
        }

        if (token == null) {

            //response status code
            return new ResponseEntity<>("access token null", HttpStatus.BAD_REQUEST);
        }
        String userName = jwtUtil.getUsername(token);

        wishListService.addWishList(productId, 1L);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/wish-lists")
    public ResponseEntity<?> deleteWishList(@RequestBody Long productId, HttpServletRequest request){

        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("access")) {

                token = cookie.getValue();
            }
        }

        if (token == null) {

            //response status code
            return new ResponseEntity<>("access token null", HttpStatus.BAD_REQUEST);
        }
        String userName = jwtUtil.getUsername(token);

        wishListService.deleteWishList(productId, userName);
        return new ResponseEntity(HttpStatus.OK);
    }
}
