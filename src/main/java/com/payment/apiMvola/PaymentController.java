package com.payment.apiMvola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private MvolaService mvolaService;

    @PostMapping("/make")
    public String makePayment(
            @RequestPart("product") String product,
            @RequestPart("clientMssidn") String clientMssidn,
            @RequestPart("refPaiement") String refPaiement,
            @RequestPart("amount") String amount) {
        String accessToken = mvolaService.authenticate();
        return mvolaService.makePayment(accessToken, product, clientMssidn, refPaiement, amount);
    }

    @PutMapping("/callback")
    public ResponseEntity<String> receiveCallback(@RequestBody String callbackData) {
        // Log the callback data or process it as needed
        System.out.println("Received callback: " + callbackData);

        // Return a response indicating that the callback was received
        return ResponseEntity.ok("Callback received successfully");
    }
}
