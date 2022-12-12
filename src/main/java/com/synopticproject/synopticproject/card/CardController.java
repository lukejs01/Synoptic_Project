package com.synopticproject.synopticproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    private CardService service;
}
