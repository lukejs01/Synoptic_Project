package com.synopticproject.synopticproject;

import com.synopticproject.synopticproject.card.Card;

public class TestConstants {


    public static Card newCard(Long employeeId) {
        Card card = new Card();
        card.setEmployeeId(employeeId);
        card.setName("TEST");
        card.setEmail("TEST");
        card.setMobileNumber("07111111111");
        card.setPin(1234);

        return card;
    }
}
