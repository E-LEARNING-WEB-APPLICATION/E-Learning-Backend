package com.learnease.server.repository;

import com.learnease.server.model.Booking;
import com.learnease.server.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, UUID> {

    boolean existsByBooking(Booking booking);
}

