package com.payment.apiMvola;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.model.Callback;
import com.payment.model.MvolaTransactionRequest;
import com.payment.model.Payment;


import com.payment.repository.CallbackRepository;
import com.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MvolaService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CallbackRepository callbackRepository;

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
            System.out.println("Access token : "+responseBody.getString("access_token"));
            return responseBody.getString("access_token");
        } else {
            throw new RuntimeException("Failed to authenticate with Mvola API");
        }
    }


    public String makePayment(String accessToken, String product, String clientMssidn, String refPaiement, String amount) {

        String correlationID = generateUUID();
        String callbackurlreceived = callbackurl + "/api/payment/callback/" + correlationID;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("Version", "1.0");
        headers.set("X-CorrelationID",correlationID);
        headers.set("UserLanguage", "MG");
        headers.set("X-Callback-URL", callbackurlreceived);
        headers.set("Accept-Charset", "utf-8");

        String timestamp = generateTimestamp();

        System.out.println("My DATETIME: " + timestamp);

        String descricptionProduct = "Payment of " + product;

        List<MvolaTransactionRequest.Party> debitParty = new ArrayList<>();
        MvolaTransactionRequest.Party party1 = new MvolaTransactionRequest.Party();
        party1.setValue(clientMssidn);
        party1.setKey("msisdn");
        debitParty.add(party1);

        List<MvolaTransactionRequest.Party> creditParty = new ArrayList<>();
        MvolaTransactionRequest.Party party2 = new MvolaTransactionRequest.Party();
        party2.setValue(creditPartieMssidn);
        party2.setKey("msisdn");
        creditParty.add(party2);


        MvolaTransactionRequest.Metadata metadata1 = new MvolaTransactionRequest.Metadata();
        metadata1.setKey("partnerName");
        metadata1.setValue(NameEntreprise); // Handle the single quote

        MvolaTransactionRequest.Metadata metadata2 = new MvolaTransactionRequest.Metadata();
        metadata2.setValue("USD");
        metadata2.setKey("fc");



        MvolaTransactionRequest.Metadata metadata3 = new MvolaTransactionRequest.Metadata();
        metadata3.setValue("1");
        metadata3.setKey("amountFc");


        List<MvolaTransactionRequest.Metadata> metadataList = new ArrayList<>();
        metadataList.add(metadata1);
        metadataList.add(metadata2);
        metadataList.add(metadata3);


        MvolaTransactionRequest request = new MvolaTransactionRequest(amount,"Ar",descricptionProduct,refPaiement,timestamp,refPaiement,debitParty,creditParty,metadataList);

        HttpEntity<String> entity = new HttpEntity<>(request.toJSON(), headers);

        try {
            System.out.println("Sending request to Mvola service: " + request.toJSON());
            ResponseEntity<String> response = restTemplate.postForEntity(transactionUrl, entity, String.class);

            Payment payment = new Payment();
            payment.setAccessToken(accessToken);
            payment.setProduct(product);
            payment.setClientMssidn(clientMssidn);
            payment.setRefPaiement(refPaiement);
            payment.setAmount(String.valueOf(amount));
            payment.setRequestDate(LocalDateTime.now());
            payment.setCallbackReceived(false);
            payment.setCorrelationID(correlationID);
            paymentRepository.save(payment);

            System.out.println(response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                String responseBody = response.getBody();
                return stringToJson(responseBody);
            } else {
                throw new RuntimeException("Failed to make payment with Mvola API: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error response from Mvola service: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to make payment with Mvola API: " + e.getResponseBodyAsString(), e);
        }
    }

    public void receiveCallback(String correlationID, String callbackData) {
        Optional<Payment> paymentOptional = paymentRepository.findByCorrelationID(correlationID);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setCallbackReceived(true);
            payment.setCallbackDate(LocalDateTime.now());
            paymentRepository.save(payment);

            String refPaiement = payment.getRefPaiement();

            System.out.println(refPaiement);

            // Save the callback data
            Callback callback = new Callback(refPaiement, callbackData, LocalDateTime.now());
            callbackRepository.save(callback);
        } else {
            throw new RuntimeException("Payment not found with ID: " + correlationID);
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

    public static String stringToJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return String.valueOf(objectMapper.readTree(jsonString));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
