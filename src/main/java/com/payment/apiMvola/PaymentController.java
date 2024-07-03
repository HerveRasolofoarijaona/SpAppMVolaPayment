package com.payment.apiMvola;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private MvolaService mvolaService;

    @PostMapping("/make")
    public String makePayment(@RequestBody String transactionDetails) {
        String accessToken = mvolaService.authenticate();
        return mvolaService.makePayment(accessToken, transactionDetails);
    }
}
