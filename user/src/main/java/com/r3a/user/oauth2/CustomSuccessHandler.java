package com.r3a.user.oauth2;

import com.r3a.user.dto.CustomOAuth2User;
import com.r3a.user.jwt.JWTUtil;
import com.r3a.user.jwt.RefreshToken;
import com.r3a.user.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, 60*60*60L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));

        RefreshToken redis = new RefreshToken(refresh, username);
        refreshTokenRepository.save(redis);
//        refreshTokenRepository.
        response.sendRedirect("http://localhost:8080/my");//원래 프론트 http://localhost:3000 로 리다이렉트
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true); //https 사용시 설정
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
