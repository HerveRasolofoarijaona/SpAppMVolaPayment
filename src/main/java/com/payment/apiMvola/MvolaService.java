package com.payment.apiMvola;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

@Service
public class MvolaService {

    @Value("${mvola.auth.url}")
    private String authUrl;

    @Value("${mvola.transaction.url}")
    private String transactionUrl;

    @Value("${mvola.client.id}")
    private String clientId;

    @Value("${mvola.client.secret}")
    private String clientSecret;

    public String authenticate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject request = new JSONObject();
        request.put("client_id", clientId);
        request.put("client_secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(authUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody.getString("access_token");
        } else {
            throw new RuntimeException("Failed to authenticate with Mvola API");
        }
    }

    public String makePayment(String accessToken, String transactionDetails) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(transactionDetails, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(transactionUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to make payment with Mvola API");
        }
    }
}
