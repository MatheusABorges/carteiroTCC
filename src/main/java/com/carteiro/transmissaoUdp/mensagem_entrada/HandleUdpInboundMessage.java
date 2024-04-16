package com.carteiro.transmissaoUdp.mensagem_entrada;

import com.carteiro.protos.JogadorOuterClass.*;
import com.carteiro.protos.MessageWrapperOuterClass.*;
import com.google.protobuf.Duration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.FileOutputStream;
import java.time.Instant;

public class HandleUdpInboundMessage {

    public void handleMessage(byte[] message) {
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
//        MessageWrapper jogadorMessage = MessageWrapper.newBuilder()
//                .setData(jogador.toByteString())
//                .setType("JOGADOR")
//                .build();
//        try (FileOutputStream output = new FileOutputStream("jogador_message_wrapped.bin")) {
//            jogadorMessage.writeTo(output);
//            System.out.println("Jogador message written to jogador_message.bin");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // Process the incoming UDP message
        try {
            MessageWrapper jogadorMessage = MessageWrapper.parseFrom(message);
            System.out.println(jogadorMessage.getType());
            Jogador jogador = null;
            switch (jogadorMessage.getType()){
                case "JOGADOR": jogador = Jogador.parseFrom(jogadorMessage.getData());break;
                default: System.out.println("tipo de mensagem não definido");
            }
            System.out.println(jogador.toString());
        }catch (Exception e){
            //System.out.println("Erro no parsing");
            System.out.println(new String(message));
        }
    }
}