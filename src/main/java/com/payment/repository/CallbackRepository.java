package com.payment.repository;

import com.payment.model.Callback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallbackRepository extends JpaRepository<Callback, Long> {
}