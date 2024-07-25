package com.payment.apiMvola;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private MvolaService mvolaService;

    @PostMapping("/make")
    public String makePayment(@RequestBody String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        String product = jsonObject.getString("product");
        String clientMssidn = jsonObject.getString("clientMssidn");
        String refPaiement = jsonObject.getString("refPaiement");
        String amount = jsonObject.getString("amount");

        System.out.println("I'm connecting with you !! SHIIT ");
        String accessToken = mvolaService.authenticate();
        return mvolaService.makePayment(accessToken, product, clientMssidn, refPaiement, amount);
    }

    @PutMapping("/callback/{paymentId}")
    public ResponseEntity<String> receiveCallback(@PathVariable String paymentId, @RequestBody String callbackData) {
        System.out.println("Received callback: " + callbackData);
        mvolaService.receiveCallback(paymentId, callbackData);
        return ResponseEntity.ok("Callback received successfully");
    }
}
