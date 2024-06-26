package com.carteiro.controllers;

import com.carteiro.models.Subscription;
import com.carteiro.models.SubscriptionDTO;
import com.carteiro.services.UdpSubscriptionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("subscription")
public class SubscriptionController {

    private final UdpSubscriptionService udpSubscriptionService;
    SpelExpressionParser parser;

    public SubscriptionController( UdpSubscriptionService udpSubscriptionService) {
        this.udpSubscriptionService = udpSubscriptionService;
        this.parser = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this.getClass().getClassLoader()));
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public UUID receiveData(@RequestBody SubscriptionDTO data) {
        Expression expr;
        try {
            expr = parser.parseExpression(data.getTextFilter());
        }catch (ParseException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given filter expression could not be parsed", e);
        }

        Subscription subscription = new Subscription(data, expr);
        udpSubscriptionService.addClientConfiguration(subscription.getId(),subscription);
        return subscription.getId();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Subscription> getAllSubscriptions(){
        return udpSubscriptionService.getAllSubscriptions();
    }

    @GetMapping("/size")
    @ResponseStatus(HttpStatus.OK)
    public int getNumberOfSubscriptions(){
        return udpSubscriptionService.getAllSubscriptions().size();
    }

    @PostMapping("/send_to_everyone")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToEveryone(@RequestBody String message){
        udpSubscriptionService.sendMessageToEveryone(message.getBytes(),"Sample ID");
    }
}
