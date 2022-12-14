package com.synopticproject.synopticproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Random;

@Service
public class CardService {

    @Autowired
    private CardRepository repository;

    /*
        - Service method for saving card
        - Validation on the card is completed here
        - If card fails validation the service should return http response
     */

    /**
     * @should return incorrect number if the amount of digits are wrong
     * @should return invalid pin if pin does not match
     * @should return http status CREATED if card was created
     * @should register card
     */
    public String registerCard(Card card) {
        card.setCardId(validateCardId());

        if (!validateMobileNumber(card.getMobileNumber())) {
            return "Incorrect mobile number";
        }
        card.setMobileNumber(validatePrefixMobileNumber(card.getMobileNumber()));

        if (!validatePinForRegistering(card.getPin())) {
            return "Invalid pin number";
        }

        repository.save(card);
        return "CREATED";
    }


    /*
        - Validates card pin to login
        - Changes the status of the card to live so transactions can be made
        - Tracks the time between taps to determine timeout time
        - Returns strings which are messages to the user
     */
    /**
     * @should return register card if not found
     * @should return incorrect pin if pin is wrong
     * @should return card timed out on next tap if its been over 2 minutes
     * @should say goodbye on second tap of card
     * @should say welcome on the first tap of card
     */
    @Transactional
    public String tap(String cardId, int pin) {
        Card card;

        if (repository.findById(cardId).isPresent()) {
            card = repository.findById(cardId).get();
        } else {
            return "You need to register this card";
        }

        if (pin != card.getPin()) {
            return "Incorrect Pin";
        }

        if (card.isLiveStatus()) {
            if (!isStillLive(card.getLastLogin())){
                repository.changeLive(false, card.getCardId());
                return "Card transaction timed out";
            }
            repository.changeLive(false, card.getCardId());
            return "Goodbye";
        }

        if (!card.isLiveStatus()) {
            repository.changeLive(true, card.getCardId());
            repository.updateLastLogin(Instant.now(), card.getCardId());
        }

        return "Welcome";
    }

    /*
        - Generates a random 16 alphanumeric ID
        - Uses character list and pulls a random character from the list
        - returns the new ID
     */
    private String generateCardId() {
        Random random = new Random();
        String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String id = "";

        for (int i = 0; i < 16; i++) {
            int randNum = random.nextInt(62);
            id += character.charAt(randNum);
        }
        return id;
    }

    /*
        - Validates the new generated ID
        - Calls generateCardId() and tries to find that id in the DB
        - If it is not found then it will return that id
        - If it is found then the process will be repeated until one is not found
     */
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


    /*
        - A function to convert any number starting in 44 to 07
        - Then returns that new number
     */
    private String validatePrefixMobileNumber(String number) {
        if (number.startsWith("44")) {
            number = number.substring(2);
            number = "0" + number;
        }
        return number;
    }


    /*
        - Completes a few validation checks before the number is saved in service function
        - Returns false if there are any errors
     */
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

    /*
        - Checks whether the pin is 4 digits long
     */
    private boolean validatePinForRegistering(int pin) {
        if (String.valueOf(pin).length() != 4) {
            return false;
        }
        return true;
    }

    /*
        - Measure taken as H2 removes the 0 from the beginning of numbers when saved
        - Will add a 0 the number has be retrieved from the db
     */
    private String getFormattedMobileNumber(String number) {
        return "0" + number;
    }

    /*
        - Compares time from last login to next use to determine the time between
        - Over 2 minutes will return a false result and indicates inactivity
     */
    private boolean isStillLive(Instant lastLogin) {
        Long lastLoginInstant = lastLogin.getEpochSecond();
        Long nowInstant = Instant.now().getEpochSecond();

        Long difference = nowInstant - lastLoginInstant;

        if (difference > 120) {
            return false;
        }

        return true;
    }
}
