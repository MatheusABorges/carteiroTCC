package com.carteiro.controllers;

import com.carteiro.models.Subscription;
import com.carteiro.models.SubscriptionDTO;
import com.carteiro.services.UdpSubscriptionService;
import com.carteiro.transmissaoUdp.mensagem_entrada.DiscardHandler;
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
            expr = !data.getTextFilter().isBlank() ? parser.parseExpression(data.getTextFilter()) : null;
        }catch (ParseException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given filter expression could not be parsed", e);
        }
        if(data.getFrequency() != null && data.getFrequency().equals(0d)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Frequency cannot be equals to 0");
        }

        if((data.getFrequency() != null || data.getPeriod() != null) && data.getMinTimeIntervalInMicroSecs() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need to choose between relative and absolute frequency, cannot have both");
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

    @GetMapping("/message_loss_count")
    @ResponseStatus(HttpStatus.OK)
    public int getMessageLossCount(){return DiscardHandler.getDiscardCount();}

    @PostMapping("/send_to_everyone")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessageToEveryone(@RequestBody String message){
        udpSubscriptionService.sendMessageToEveryone(message.getBytes(),"Sample ID");
    }

    @DeleteMapping("/unsubscribe/{subscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void clearSubscriptionsData(@PathVariable(value="subscriptionId") String subscriptionId){
        udpSubscriptionService.deleteSubscription(UUID.fromString(subscriptionId));
    }
}
