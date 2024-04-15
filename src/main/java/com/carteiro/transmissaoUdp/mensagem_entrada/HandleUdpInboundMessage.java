package com.carteiro.transmissaoUdp.mensagem_entrada;

import com.carteiro.protos.JogadorOuterClass.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.time.Instant;

public class HandleUdpInboundMessage {

    public void handleMessage(Message<byte[]> message) {
//        Jogador jogador = Jogador.newBuilder()
//                .setId(13)
//                .setRegiaoDoServidor("Brazil")
//                .setVelocidade(15.5f)
//                .setPosicao(2)
//                .setTempoDeCorrida(Duration.newBuilder().setSeconds(120).setNanos(5465465).build())
//                .setPistaDeCorrida(Jogador.PistaDaCorrida.INTERLAGOS)
//                .setLocalizacao(Localizacao.newBuilder().setLatitude(1).setLongitude(1).setAltitude(2).build())
//                .setCarro(Jogador.Carro.newBuilder()
//                        .setEstadoDoCarro(0.8f)
//                        .setCor(Color.newBuilder().setRed(1).setBlue(0).setGreen(1).setAlpha(1).build())
//                        .setModeloDoCarro(Jogador.Carro.ModeloCarro.FUSCA)
//                )
//                .build();
//
//        try (FileOutputStream output = new FileOutputStream("jogador_message.bin")) {
//            jogador.writeTo(output);
//            System.out.println("Jogador message written to jogador_message.bin");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // Process the incoming UDP message
        byte[] payload = message.getPayload();
        MessageHeaders mh = message.getHeaders();
        //Date date = new Date((Long) mh.get("timestamp"));
        try {
            Jogador jogador = Jogador.parseFrom(payload);
            Instant now = Instant.now();
            System.out.println("Latência: " + now.minusMillis((Long)mh.get("timestamp")));
            System.out.println(jogador.toString());
        }catch (Exception e){
            //System.out.println("Erro no parsing");
            System.out.println(new String(payload));
        }
    }
}