package com.synopticproject.synopticproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CardService {

    @Autowired
    private CardRepository repository;

    private String generateCardId(){
        Random random = new Random();
        String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String id = "";

        for (int i = 0; i < 15; i++) {
            int randNum = random.nextInt(36);
            id += character.charAt(randNum);
        }
        return id;
    }

    private String validateCardId(){
        String id = "";
        boolean state = false;

        while (!state){
            id = generateCardId();
            var toCheck = repository.findById(id);
            if (toCheck.isEmpty()){
                state = true;
            }
        }
        return id;
    }

    private Long generateEmployeeId(){
        Long lastId = repository.findTopByOrderByEmployeeIdDesc();

        return lastId + 1L;
    }

    private boolean validateMobileNumber(Long number){
        String numToString = number.toString();

        if (numToString.startsWith("44")){
            numToString = numToString.substring(2);
            numToString = "0" + numToString;
        }
        if (!numToString.startsWith("07")){
            return false;
        }
        if (numToString.length() != 11){
            return false;
        }

        return true;
    }
}
