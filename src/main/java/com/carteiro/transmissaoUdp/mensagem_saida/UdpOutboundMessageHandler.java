package com.carteiro.transmissaoUdp.mensagem_saida;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

public class UdpOutboundMessageHandler {

    DatagramSocket socket;

    public UdpOutboundMessageHandler(){
        try {
            this.socket = new DatagramSocket();
        }catch (Exception e){
            System.out.println("Socket could not be instantiated on UdpOutboundMessageHandler");
        }
    }

    public void sendMessage(byte[] message, InetAddress ip, int port, String messageId, UUID subscriptionId) {
        try {
            DatagramPacket packetSend = new DatagramPacket(message, message.length, ip, port);
            socket.send(packetSend);
        } catch(Exception e){
            System.out.println("Failed to send message to: " + ip.toString() + ":" + port + "\nMessage ID: " + messageId + " subscriptionId: " + subscriptionId.toString());
        }
    }

}