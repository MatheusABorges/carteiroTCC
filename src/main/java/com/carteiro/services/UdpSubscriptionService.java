package com.carteiro.services;

import com.carteiro.models.Subscription;
import com.carteiro.transmissaoUdp.mensagem_saida.UdpOutboundMessageHandler;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

@Service
public class UdpSubscriptionService {

    private static final ConcurrentHashMap<UUID, Subscription> subscriptionMap = new ConcurrentHashMap<>();

    private static Map<String, Method> parseMap;

    private static final ConcurrentHashMap<UUID, Integer> periodMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<UUID, Instant> absoluteFrequencyMap = new ConcurrentHashMap<>();

    @Value("${protosTypes}")
    private List<String> protosNamingList;

    //Inicia o map que guarda uma referência para a função de parse dos protos cujos nomes foram definidos na variável protosNamingList em application.properties
    @PostConstruct
    public void init(){
        parseMap = new HashMap<>();
        // Convert class names to actual classes
        for (String className : protosNamingList) {
            try {
                Class<?> clazz = Class.forName("com.carteiro.protos." + className + "OuterClass$" + className);
                Method parseFromMethod = clazz.getMethod("parseFrom", ByteString.class);
                parseMap.put(className, parseFromMethod);

                //Garanto que o retorno do metodo parseFrom é um GeneratedMessageV3
                if (!GeneratedMessageV3.class.isAssignableFrom(parseFromMethod.getReturnType())) {
                    throw new IllegalArgumentException("Method \"parseFrom\" from class \" "+ className + "\" return type is not GeneratedMessageV3 or its subclass");
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("The class named \"" + className + "\" could not be found");
            } catch(NoSuchMethodException e){
                throw new RuntimeException("The class named \"" + className + "\" does not contains the method \"parseFrom\"");
            } catch(IllegalArgumentException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        // Create an unmodifiable view of the map after initialization
        parseMap = Collections.unmodifiableMap(parseMap);
        protosNamingList = null;
    }

    public void addClientConfiguration(UUID clientId, Subscription subscription) {
        subscriptionMap.put(clientId, subscription);
        if (subscription.getPeriod() != null || subscription.getFrequency() !=null ){
            periodMap.put(clientId, 0);
        }
        if(subscription.getMinTimeIntervalInNanoSecs()!= null){
            absoluteFrequencyMap.put(clientId,Instant.MIN);
        }
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

    public static Collection<Subscription> getAllSubscriptions(){
        return subscriptionMap.values();
    }

    public int getPeriodCounter(UUID subscriptionId) {
        int currentPeriod = periodMap.get(subscriptionId);
        periodMap.computeIfPresent(subscriptionId, (k, v) -> v+1);
        return currentPeriod;
    }

    public Instant getLastTimeClientReceived(UUID subscriptionId){
        return absoluteFrequencyMap.get(subscriptionId);
    }

    public void setLastTimeClientReceived(UUID subscriptionId,Instant instant){
        absoluteFrequencyMap.computeIfPresent(subscriptionId, (k,v) -> instant);
    }

    public Method getParseByName(String name) {
        try {
            return parseMap.get(name);
        } catch(NullPointerException e){
            System.out.println("Type of message \"" + name + "\" does not exist");
            return null;
        }
    }
}
