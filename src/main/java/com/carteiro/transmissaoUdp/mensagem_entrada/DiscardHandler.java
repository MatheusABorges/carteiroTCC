package com.carteiro.transmissaoUdp.mensagem_entrada;



import java.util.concurrent.atomic.AtomicInteger;



public class DiscardHandler {

    private static AtomicInteger discardCount = new AtomicInteger(0);

    public void handle(Byte[] message) {
        int currentCount = discardCount.incrementAndGet();
    }

    public static int getDiscardCount() {
        return discardCount.get();
    }
}
