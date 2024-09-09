package com.carteiro.transmissaoUdp.mensagem_entrada;

import com.carteiro.models.Subscription;
import com.carteiro.protos.MessageWrapperOuterClass.MessageWrapper;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.carteiro.services.UdpSubscriptionService;
import com.carteiro.transmissaoUdp.mensagem_saida.UdpOutboundMessageHandler;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import jakarta.annotation.PostConstruct;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HandleUdpInboundMessage {

    private final UdpSubscriptionService udpSubscriptionService;

    private final UdpOutboundMessageHandler udpOutboundMessageHandler;

    public HandleUdpInboundMessage(UdpSubscriptionService udpSubscriptionService) {
        this.udpSubscriptionService = udpSubscriptionService;
        udpOutboundMessageHandler = new UdpOutboundMessageHandler();
    }

    @PostConstruct
    public void startReceiver() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(9876)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    byte[] data = new byte[packet.getLength()];
                    System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());

                    ByteArrayInputStream byteStream = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
                    handleMessage(byteStream.readAllBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Async
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

            GeneratedMessageV3 message = null;//(GeneratedMessageV3)parseMethod.invoke(null, outerMessage.getData());

            StandardEvaluationContext context = new StandardEvaluationContext();
//            UdpSubscriptionService.getAllSubscriptions().forEach(
//                    subscription -> {
//                        Boolean shouldSend = true;
//                        Boolean passedAbsoluteSampling = false;
//                        Instant currentTime = Instant.now();
//                        //checks if the client is subscribed to this type of message
//                        if(subscription.getMessageType().equals(outerMessage.getType())){
//                            //sampling
//                            if(subscription.getPeriod() != null) {
//                                if(udpSubscriptionService.getPeriodCounter(subscription.getId())%subscription.getPeriod() != 0){
//                                    shouldSend = false;
//                                }
//                            } else if(subscription.getFrequency() != null && ThreadLocalRandom.current().nextDouble() > subscription.getFrequency()) {
//                                shouldSend = false;
//                            }
//                            //Absolute frequency sampling
//
//                            if (subscription.getMinTimeIntervalInNanoSecs()!= null){
//                                long duration = Duration.between(udpSubscriptionService.getLastTimeClientReceived(subscription.getId()), currentTime).toNanos();
//                                if(duration  < subscription.getMinTimeIntervalInNanoSecs()){
//                                    shouldSend = false;
//                                }
//                                else {
//                                    passedAbsoluteSampling = true;
//                                }
//                            }
//                            //Content based filtering
//                            if(subscription.getContentFilter() != null &&  shouldSend) {
//                                context.setRootObject(message);
//                                shouldSend = (Boolean) subscription.getContentFilter().getValue(context);
//                            }
//
//                        }else {
//                            shouldSend = false;
//                        }
//                        if(shouldSend != null && shouldSend) {
//                            udpOutboundMessageHandler.sendMessage(rawMessage, subscription.getIp(), subscription.getPort(), "<ID PLACEHOLDER>", subscription.getId());
//                            if (passedAbsoluteSampling)
//                                udpSubscriptionService.setLastTimeClientReceived(subscription.getId(),currentTime);
//                        }
//                    }
//            );
            Collection<Subscription> subscriptions = UdpSubscriptionService.getAllSubscriptions();
            for (Subscription subscription : subscriptions) {
                Boolean shouldSend = true;
                Boolean passedAbsoluteSampling = false;
                Instant currentTime = Instant.now();

                // Checks if the client is subscribed to this type of message
                if (subscription.getMessageType().equals(outerMessage.getType())) {
                    // Sampling
                    if (subscription.getPeriod() != null) {
                        if (udpSubscriptionService.getPeriodCounter(subscription.getId()) % subscription.getPeriod() != 0) {
                            shouldSend = false;
                        }
                    } else if (subscription.getFrequency() != null && ThreadLocalRandom.current().nextDouble() > subscription.getFrequency()) {
                        shouldSend = false;
                    }

                    // Absolute frequency sampling
                    if (subscription.getMinTimeIntervalInNanoSecs() != null) {
                        long duration = Duration.between(udpSubscriptionService.getLastTimeClientReceived(subscription.getId()), currentTime).toNanos();
                        if (duration < subscription.getMinTimeIntervalInNanoSecs()) {
                            shouldSend = false;
                        } else {
                            passedAbsoluteSampling = true;
                        }
                    }

                    // Content-based filtering
                    if (subscription.getContentFilter() != null && shouldSend) {
                        message = (GeneratedMessageV3)parseMethod.invoke(null, outerMessage.getData());
                        context.setRootObject(message);
                        shouldSend = (Boolean) subscription.getContentFilter().getValue(context);
                    }

                } else {
                    shouldSend = false;
                }

                if (shouldSend != null && shouldSend) {
                    udpOutboundMessageHandler.sendMessage(rawMessage, subscription.getIp(), subscription.getPort(), "<ID PLACEHOLDER>", subscription.getId());
                    if (passedAbsoluteSampling) {
                        udpSubscriptionService.setLastTimeClientReceived(subscription.getId(), currentTime);
                    }
                }
            }

        } catch(InvalidProtocolBufferException e){
            System.out.println("Message could not be parsed");
        }
        catch (Exception e){
            //System.out.println("Erro no parsing");
            System.out.println(new String(rawMessage));
        }
    }
}