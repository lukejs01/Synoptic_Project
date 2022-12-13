package com.synopticproject.synopticproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CardService {

    @Autowired
    private CardRepository repository;


    public HttpStatus registerCard(Card card) {
        card.setCardId(validateCardId());

        if (!validateMobileNumber(card.getMobileNumber())) {
            return HttpStatus.valueOf("Incorrect mobile number");
        }
        card.setMobileNumber(validatePrefixMobileNumber(card.getMobileNumber()));

        repository.save(card);
        return HttpStatus.CREATED;
    }


    private String generateCardId() {
        Random random = new Random();
        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String id = "";

        for (int i = 0; i < 15; i++) {
            int randNum = random.nextInt(36);
            id += character.charAt(randNum);
        }
        return id;
    }

    private String validateCardId() {
        String id = "";
        boolean state = false;

        while (!state) {
            id = generateCardId();
            var toCheck = repository.findById(id);
            if (toCheck.isEmpty()) {
                state = true;
            }
        }
        return id;
    }


    private String validatePrefixMobileNumber(String number) {
        if (number.startsWith("44")) {
            number = number.substring(2);
            number = "0" + number;
        }
        return number;
    }

    private boolean validateMobileNumber(String number) {
        number = validatePrefixMobileNumber(number);
        if (!number.startsWith("07")) {
            return false;
        }
        if (number.length() != 11) {
            return false;
        }
        return true;
    }

    private String getFormattedMobileNumber(String number){
        return "0" + number;
    }
}
