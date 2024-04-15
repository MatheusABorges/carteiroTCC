package com.carteiro.controllers;

import com.carteiro.models.Subscription;
import com.carteiro.models.SubscriptionDTO;
import com.carteiro.services.UdpSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("subscription")
public class SubscriptionController {

    private final UdpSubscriptionService udpSubscriptionService;

    public SubscriptionController( UdpSubscriptionService udpSubscriptionService) {
        this.udpSubscriptionService = udpSubscriptionService;
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public UUID receiveData(@RequestBody SubscriptionDTO data) {
        // Store the received data in the ConcurrentHashMap
        Subscription subscription = new Subscription(data);
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
