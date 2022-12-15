package com.synopticproject.synopticproject.card;

import com.synopticproject.synopticproject.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CardServiceTest {

    @Autowired
    private CardService service;

    @Autowired
    private CardRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }


    /**
     * @verifies return incorrect number if the amount of digits are wrong
     * @see CardService#registerCard(Card)
     */
    @Test
    public void registerCard_shouldReturnIncorrectNumberIfTheAmountOfDigitsAreWrong() {
        Card card = TestConstants.newCard(1L);
        card.setCardId("1234567812345678");
        card.setMobileNumber("22324242");

        Assertions.assertEquals("Incorrect mobile number", service.registerCard(card));

    }

    /**
     * @verifies return invalid pin if pin does not match
     * @see CardService#registerCard(Card)
     */
    @Test
    public void registerCard_shouldReturnInvalidPinIfPinDoesNotMatch() {
        Card card = TestConstants.newCard(1L);
        card.setCardId("1234567812345678");
        card.setPin(1234444);

        Assertions.assertEquals("Invalid pin number", service.registerCard(card));
    }

    /**
     * @verifies return http status CREATED if card was created
     * @see CardService#registerCard(Card)
     */
    @Test
    public void registerCard_shouldReturnCREATEDIfCardWasCreated() {
        Card card = TestConstants.newCard(1L);
        card.setCardId("1234567812345678");

        Assertions.assertEquals("CREATED", service.registerCard(card));
    }

    /**
     * @verifies register card
     * @see CardService#registerCard(Card)
     */
    @Test
    public void registerCard_shouldRegisterCard() {
        Card card = TestConstants.newCard(1L);

        service.registerCard(card);

        Card result = repository.findAll().get(0);
        Assertions.assertEquals("TEST", result.getName());
    }

    /**
     * @verifies return register card if not found
     * @see CardService#tap(String, int)
     */
    @Test
    public void tap_shouldReturnRegisterCardIfNotFound() {
        Assertions.assertEquals
                (service.tap("1234567812345678", 1234), "You need to register this card");
    }

    /**
     * @verifies return incorrect pin if pin is wrong
     * @see CardService#tap(String, int)
     */
    @Test
    public void tap_shouldReturnIncorrectPinIfPinIsWrong() {
        Card card = TestConstants.newCard(1L);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        // Correct pin is set in TestConstants as 1234
        Assertions.assertEquals("Incorrect Pin", service.tap(cardId, 4321));

    }

    /**
     * @verifies return card timed out on next tap if its been over 2 minutes
     * @see CardService#tap(String, int)
     */
    @Disabled("Needs fixing")
    @Transactional
    @Test
    public void tap_shouldReturnCardTimedOutOnNextTapIfItsBeenOver2Minutes() {
        Card card = TestConstants.newCard(1L);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        service.tap(cardId, 1234);
        Instant newLoginTime = card.getLastLogin().plus(130, ChronoUnit.SECONDS);
        repository.updateLastLoginForTest(newLoginTime, cardId);
        Assertions.assertEquals("Card transaction timed out", service.tap(cardId, 1234));
    }

    /**
     * @verifies say goodbye on second tap of card
     * @see CardService#tap(String, int)
     */
    @Test
    public void tap_shouldSayGoodbyeOnSecondTapOfCard() {
        Card card = TestConstants.newCard(1L);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        service.tap(cardId, 1234);
        Assertions.assertEquals("Goodbye", service.tap(cardId, 1234));
    }

    /**
     * @verifies say welcome on the first tap of card
     * @see CardService#tap(String, int)
     */
    @Test
    public void tap_shouldSayWelcomeOnTheFirstTapOfCard() {
        Card card = TestConstants.newCard(1L);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        Assertions.assertEquals("Welcome " + card.getName(), service.tap(cardId, 1234));
    }

    /**
     * @verifies return insufficient funds if purchase is bigger than balance
     * @see CardService#makePurchase(String, Double)
     */
    @Test
    public void makePurchase_shouldReturnInsufficientFundsIfPurchaseIsBiggerThanBalance() {
        Card card = TestConstants.newCard(1L);
        card.setBalance(100.0);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        service.tap(cardId, 1234);

        Assertions.assertEquals("Insufficient funds for purchase", service.makePurchase(cardId, 110.0));
    }

    /**
     * @verifies make purchase if validations pass
     * @see CardService#makePurchase(String, Double)
     */
    @Test
    public void makePurchase_shouldMakePurchaseIfValidationsPass() {
        Card card = TestConstants.newCard(1L);
        card.setBalance(100.0);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        service.tap(cardId, 1234);
        Assertions.assertEquals("Your purchase has been successful", service.makePurchase(cardId, 50.0));
        service.makePurchase(cardId,50.0);
        Assertions.assertEquals(50.0, repository.findAll().get(0).getBalance());
    }

    /**
     * @verifies top up card and return message
     * @see CardService#topUp(String, Double)
     */
    @Test
    public void topUp_shouldTopUpCardAndReturnMessage() {
        Card card = TestConstants.newCard(1L);
        service.registerCard(card);
        String cardId = repository.findAll().get(0).getCardId();

        service.tap(cardId, 1234);
        Assertions.assertEquals("Your card has been topped up", service.topUp(cardId, 50.0));
        Assertions.assertEquals(50.0, repository.findAll().get(0).getBalance());
    }
}
