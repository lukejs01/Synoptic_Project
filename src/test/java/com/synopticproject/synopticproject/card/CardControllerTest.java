package com.synopticproject.synopticproject.card;

import com.synopticproject.synopticproject.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CardControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CardRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }


    /**
     * @verifies save card if data is correct
     * @see CardController#registerCard(Card)
     */
    @Test
    public void registerCard_shouldSaveCardIfDataIsCorrect() {
        Card card = TestConstants.newCard(1L);

        WebTestClient.ResponseSpec response = webTestClient
                .post().uri("/card/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(card), Card.class)
                .exchange();

        response.expectStatus().isOk();

        Assertions.assertEquals(16, repository.findAll().get(0).getCardId().length());
        Assertions.assertEquals("TEST", repository.findAll().get(0).getName());
    }

    /**
     * @verifies return either welcome or goodbye depending on the status of the card
     * @see CardController#tap(String, int)
     */
    @Test
    public void tap_shouldReturnEitherWelcomeOrGoodbyeDependingOnTheStatusOfTheCard() {

        Card card = TestConstants.newCard(1L);

        WebTestClient.ResponseSpec saveCard = webTestClient
                .post().uri("/card/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(card), Card.class)
                .exchange();
        Assertions.assertEquals(16, repository.findAll().get(0).getCardId().length());
        String cardId = repository.findAll().get(0).getCardId();

        WebTestClient.ResponseSpec response = webTestClient
                .get().uri("/card/tap/{cardId}/{pin}", cardId, card.getPin())
                .exchange();

        response.expectStatus().isOk();


        String result = response
                .returnResult(String.class)
                .getResponseBody()
                .single()
                .block();

        Assertions.assertEquals("Welcome", result);
    }

    /**
     * @verifies return goodbye when the card is active
     * @see CardController#tap(String, int)
     */
    @Test
    public void tap_shouldReturnGoodbyeWhenTheCardIsActive() {
        Card card = TestConstants.newCard(1L);

        WebTestClient.ResponseSpec saveCard = webTestClient
                .post().uri("/card/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(card), Card.class)
                .exchange();
        Assertions.assertEquals(16, repository.findAll().get(0).getCardId().length());
        String cardId = repository.findAll().get(0).getCardId();

        WebTestClient.ResponseSpec welcomeTap = webTestClient
                .get().uri("/card/tap/{cardId}/{pin}", cardId, card.getPin())
                .exchange();

        WebTestClient.ResponseSpec response = webTestClient
                .get().uri("/card/tap/{cardId}/{pin}", cardId, card.getPin())
                .exchange();

        response.expectStatus().isOk();


        String result = response
                .returnResult(String.class)
                .getResponseBody()
                .single()
                .block();

        Assertions.assertEquals("Goodbye", result);
    }
}
