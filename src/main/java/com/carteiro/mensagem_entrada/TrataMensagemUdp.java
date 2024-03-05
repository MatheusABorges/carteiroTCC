package com.carteiro.mensagem_entrada;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.time.Instant;
import java.util.Date;

public class TrataMensagemUdp {

    public void handleMessage(Message<String> message) {
        // Process the incoming UDP message
        Instant now = Instant.now();
        String payload = message.getPayload();
        MessageHeaders mh = message.getHeaders();
        Date date = new Date((Long)mh.get("timestamp"));
        final JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
        System.out.println("Received UDP message: " + payload);
    }
}