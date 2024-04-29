package com.carteiro.transmissaoUdp.mensagem_entrada;

import com.carteiro.protos.DeteccaoOuterClass.Deteccao;
import com.carteiro.protos.JogadorOuterClass;
import com.carteiro.protos.JogadorOuterClass.Jogador;
import com.carteiro.protos.TransacaoOuterClass.Transacao;
import com.carteiro.protos.MessageWrapperOuterClass.MessageWrapper;

import com.carteiro.services.UdpSubscriptionService;
import com.carteiro.transmissaoUdp.mensagem_saida.UdpOutboundMessageHandler;
import com.google.protobuf.Duration;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;

@Component
public class HandleUdpInboundMessage {

    private final UdpSubscriptionService udpSubscriptionService;

    private final UdpOutboundMessageHandler udpOutboundMessageHandler;

    public HandleUdpInboundMessage(UdpSubscriptionService udpSubscriptionService) {
        this.udpSubscriptionService = udpSubscriptionService;
        udpOutboundMessageHandler = new UdpOutboundMessageHandler();
    }

    public void handleMessage(byte[] rawMessage) {
//        Jogador jogador = Jogador.newBuilder()
//                .setId(13)
//                .setRegiaoDoServidor("Brazil")
//                .setVelocidade(15.5f)
//                .setPosicao(2)
//                .setTempoDeCorrida(Duration.newBuilder().setSeconds(120).setNanos(5465465).build())
//                .setPistaDeCorrida(Jogador.PistaDaCorrida.INTERLAGOS)
//                .setLocalizacao(JogadorOuterClass.Localizacao.newBuilder().setLatitude(1).setLongitude(1).setAltitude(2).build())
//                .setCarro(Jogador.Carro.newBuilder()
//                        .setEstadoDoCarro(0.8f)
//                        .setCor(JogadorOuterClass.Color.newBuilder().setRed(1).setBlue(0).setGreen(1).setAlpha(1).build())
//                        .setModeloDoCarro(Jogador.Carro.ModeloCarro.FUSCA)
//                )
//                .build();
//
//        MessageWrapper jogadorMessage = MessageWrapper.newBuilder()
//                .setData(jogador.toByteString())
//                .setType("Jogador")
//                .build();
//        try (FileOutputStream output = new FileOutputStream("jogador_message_wrapped.bin")) {
//            jogadorMessage.writeTo(output);
//            System.out.println("Jogador message written to jogador_message.bin");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // Process the incoming UDP message
        try {
            MessageWrapper outerMessage = MessageWrapper.parseFrom(rawMessage);

            var parseMethod = udpSubscriptionService.getParseByName(outerMessage.getType());

            //Ignora mensagem caso o seu tipo não esteja especificado em application.properties
            if(parseMethod == null)
                return;

            GeneratedMessageV3 message = (GeneratedMessageV3)parseMethod.invoke(null, outerMessage.getData());

            StandardEvaluationContext context = new StandardEvaluationContext();
            UdpSubscriptionService.getAllSubscriptions().forEach(
                    subscription -> {
                        context.setRootObject(message);
                        if(subscription.getMessageType().equals(outerMessage.getType())){
                            var expValue = (Boolean)subscription.getContentFilter().getValue(context);
                            if(expValue != null && expValue) {
                                udpOutboundMessageHandler.sendMessage(rawMessage, subscription.getIp(), subscription.getPort(), "<ID PLACEHOLDER>", subscription.getId());
                            }
                        }
                    }
            );

        } catch(InvalidProtocolBufferException e){
            System.out.println("Message could not be parsed");
        }
        catch (Exception e){
            //System.out.println("Erro no parsing");
            System.out.println(new String(rawMessage));
        }
    }
}