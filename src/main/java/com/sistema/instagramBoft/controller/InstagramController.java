package com.sistema.instagramBoft.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    private final RestTemplate restTemplate;

    public InstagramController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> handleInstagramCallback(
            @RequestParam("code") String code) {

        // Troca do código pelo token de acesso
        String tokenUrl = "https://api.instagram.com/oauth/access_token";

        Map<String, String> body = new HashMap<>();
        body.put("client_id", "YOUR_CLIENT_ID");
        body.put("client_secret", "YOUR_CLIENT_SECRET");
        body.put("grant_type", "authorization_code");
        body.put("redirect_uri", "http://localhost:8080/instagram/oauth2/callback");
        body.put("code", code);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, body, Map.class);

        // Obtenha o token de acesso
        String accessToken = response.getBody().get("access_token").toString();

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/user-profile")
    public ResponseEntity<?> getUserProfile(@RequestParam("accessToken") String accessToken) {
        String url = "https://graph.instagram.com/me?fields=id,username&access_token=" + accessToken;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        return ResponseEntity.ok(response.getBody());
    }
}
