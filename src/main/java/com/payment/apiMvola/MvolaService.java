package com.payment.apiMvola;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

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

    @Value("${mvola.callbackurl}")
    private String callbackurl;

    @Value("${mvola.creditParty}")
    private String creditPartieMssidn;

    @Value("${mvola.NameEntreprise}")
    private String NameEntreprise;



    public String authenticate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cache-Control", "no-cache");

        // Generate Base64 encoded client_id:client_secret
        String authStr = clientId + ":" + clientSecret;
        String base64AuthStr = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers.set("Authorization", "Basic " + base64AuthStr);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "EXT_INT_MVOLA_SCOPE");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            return responseBody.getString("access_token");
        } else {
            throw new RuntimeException("Failed to authenticate with Mvola API");
        }
    }

    public String makePayment(String accessToken, String product, String clientMssidn, String refPaiement, String amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("Version", "1.0");
        headers.set("X-CorrelationID", generateUUID());
        headers.set("UserLanguage", "MG");
        headers.set("X-Callback-URL",callbackurl);
        headers.set("Accept-Charset", "utf-8");

        String timestamp = generateTimestamp();

        // Construct the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", "Ar");
        requestBody.put("descriptionText", "Payment "+ product);
        requestBody.put("requestingOrganisationTransactionReference", refPaiement);
        requestBody.put("requestDate", timestamp);
        requestBody.put("originalTransactionReference", refPaiement);

        JSONObject debitParty = new JSONObject();
        debitParty.put("key", "msisdn");
        debitParty.put("value", clientMssidn);
        requestBody.append("debitParty", debitParty);

        JSONObject creditParty = new JSONObject();
        creditParty.put("key", "msisdn");
        creditParty.put("value", creditPartieMssidn);
        requestBody.append("creditParty", creditParty);

        JSONObject metadata1 = new JSONObject();
        metadata1.put("key", "partnerName");
        metadata1.put("value", NameEntreprise);
        requestBody.append("metadata", metadata1);

        JSONObject metadata2 = new JSONObject();
        metadata2.put("key", "fc");
        metadata2.put("value", "USD");
        requestBody.append("metadata", metadata2);

        JSONObject metadata3 = new JSONObject();
        metadata3.put("key", "amountFc");
        metadata3.put("value", "1");
        requestBody.append("metadata", metadata3);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(transactionUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to make payment with Mvola API");
        }
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String generateTimestamp() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return now.format(formatter);
    }

}
