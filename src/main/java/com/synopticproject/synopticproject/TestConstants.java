package com.synopticproject.synopticproject;

import com.synopticproject.synopticproject.card.Card;

import java.time.Instant;

public class TestConstants {


    public static Card newCard(Long employeeId) {
        Card card = new Card();
        card.setEmployeeId(employeeId);
        card.setName("TEST");
        card.setEmail("TEST");
        card.setMobileNumber("07111111111");
        card.setPin(1234);
        card.setBalance(0.0);
        card.setLastLogin(Instant.now());

        return card;
    }
}
