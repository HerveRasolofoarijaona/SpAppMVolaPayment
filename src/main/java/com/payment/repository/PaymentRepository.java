package com.payment.repository;


import com.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRefPaiement(String refPaiement);
    Optional<Payment> findByCorrelationID(String correlationID);
}