package com.synopticproject.synopticproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    private CardService service;

    /*
        - Controller for saving a new card within the database
        - Values should be passed in the body of a http request
     */

    /**
     * @should save card if data is correct
     */
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.OK)
    public void registerCard(@RequestBody Card card) {
        service.registerCard(card);
    }

    /*
        - Tap API is the implementation of logging in and out of the kiosk
     */

    /**
     * @should return either welcome or goodbye depending on the status of the card
     * @should return goodbye when the card is active
     */
    @GetMapping(value = "/tap/{cardId}/{pin}")
    @ResponseBody
    public String tap(@PathVariable String cardId,
                      @PathVariable int pin) {
        return service.tap(cardId, pin);
    }

    /**
     * @should alter balance in account and confirm purchase
     */
    @PostMapping(value = "/make-purchase/{cardId}/{amount}")
    @ResponseBody
    public String makePurchase(@PathVariable String cardId,
                             @PathVariable Double amount) {
        return service.makePurchase(cardId, amount);
    }

    /**
     * @should add funds to balance and confirm transaction
     */
    @PostMapping(value = "/top-up/{cardId}/{amount}")
    @ResponseBody
    public String topUp(@PathVariable String cardId,
                      @PathVariable Double amount) {
        return service.topUp(cardId, amount);
    }
}
