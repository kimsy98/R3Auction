package com.r3a.user.service;


import com.r3a.user.config.KakaoPayProperties;
import com.r3a.user.dto.KakaoApproveResponse;
import com.r3a.user.dto.KakaoReadyResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

    private final KakaoPayProperties payProperties;
    private RestTemplate restTemplate = new RestTemplate();
    private KakaoReadyResponse kakaoReady;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + payProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    /**
     * 결제 완료 요청
     */
    public KakaoReadyResponse kakaoPayReady() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("cid", payProperties.getCid());
        parameters.put("partner_order_id", "ORDER_ID"); // 실제 주문 번호로 교체
        parameters.put("partner_user_id", "USER_ID");   // 실제 사용자 ID로 교체
        parameters.put("item_name", "충전 금액");       // 실제 상품명으로 교체
        parameters.put("quantity", "1");                 // 수량, 숫자는 문자열로 전달
        parameters.put("total_amount", "2200");          // 총 금액, 숫자는 문자열로 전달
        parameters.put("vat_amount", "200");             // 부가세, 숫자는 문자열로 전달
        parameters.put("tax_free_amount", "0");          // 비과세 금액, 숫자는 문자열로 전달
        parameters.put("approval_url", "http://localhost:8080/api/v1/payment/success");
        parameters.put("fail_url", "http://localhost:8080/api/v1/payment/fail");
        parameters.put("cancel_url", "http://localhost:8080/api/v1/payment/cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());


        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
        return kakaoReady;
    }

    public KakaoApproveResponse approveResponse (String pgToken){

        // 카카오 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", kakaoReady.getTid());
        parameters.put("partner_order_id", "ORDER_ID");
        parameters.put("partner_user_id", "USER_ID");
        parameters.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        System.out.println();
        System.out.println();
        System.out.println(requestEntity);
        System.out.println();
        System.out.println();

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(approveResponse);
        System.out.println();
        System.out.println();
        System.out.println();
        return approveResponse;
    }
}
