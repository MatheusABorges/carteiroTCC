package com.carteiro.services;

import com.carteiro.models.Subscription;
import com.carteiro.transmissaoUdp.mensagem_saida.UdpOutboundMessageHandler;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UdpSubscriptionService {

    private static final ConcurrentHashMap<UUID, Subscription> subscriptionMap = new ConcurrentHashMap<>();

    public void addClientConfiguration(UUID clientId, Subscription subscription) {
        subscriptionMap.put(clientId, subscription);
        System.out.println(clientId);
    }

    public void sendMessageToEveryone(byte[] message, String messageId) {
        UdpOutboundMessageHandler handler = new UdpOutboundMessageHandler();
        subscriptionMap.forEach((id, subscription) ->
                handler.sendMessage(message,
                                    subscription.getIp(),
                                    subscription.getPort(),
                                    messageId,
                                    subscription.getId()));
    }

    public Collection<Subscription> getAllSubscriptions(){
        return subscriptionMap.values();
    }

}
