package com.consolerouting;

import java.util.Scanner;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Hello world!
 *
 */
public class App {
    // Attribute yang dibutuhkan untuk mengirim message
    private static final String EXCHANGE_NAME = "E-Commerce";
    private static final String[] ROUTING_KEY = { "checkout", "credit" };
    private static final String[] QUEUE_NAMES = { "checkout_queue", "credit_queue" };

    public static void main(String[] args) throws Exception {
        // Pabrik koneksi ke RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            while (true) {
                // Bangun Koneksi ke RabbitMQ dari Pabrik Koneksi
                Connection koneksiRabbitMQ = factory.newConnection();
                // Channel
                Channel channel = koneksiRabbitMQ.createChannel();

                System.out.println(
                        "MENU MESSAGING" + "\n1.Generate Random Message Credit" + "\n2.Generate Random Message Checkout");
                System.out.print("Your Choice : ");
                int choice = new Scanner(System.in).nextInt();

                if (choice == 1) {
                    Credit newCredit = new Credit(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 100);
                    // Kirim message
                    sendCreditMessageToBroker(channel, newCredit);
                } else if (choice == 2) {
                    Checkout newCheckout = new Checkout(UUID.randomUUID().toString(), "Payment_token_lazada_21");
                    // Kirim Message
                    sendCheckoutMessageToBroker(channel, newCheckout);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void sendCheckoutMessageToBroker(Channel channel, Checkout messagePayload) throws Exception {
        // Mendeklarasikan Exchange -> Tujuan : Memberitahu Broker ke Exchange mana
        // Message dikirimkan
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // Mendeklarasikan Queue -> Tujuan : Memberitahu broker ke Queue mana Message
        // dikirimkan
        channel.queueDeclare(QUEUE_NAMES[0], true, false, false, null);
        // Membuat Binding / Link
        channel.queueBind(QUEUE_NAMES[0], EXCHANGE_NAME, ROUTING_KEY[0]);

        // Publish Message
        // Konversi messagePayload -> JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(messagePayload);

        // Kirim ke Broker
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY[0], null, jsonMessage.getBytes());
        System.out.println("[>>] Message Checkout Sudah Dikirim Ke Broker!");
    }

    static void sendCreditMessageToBroker(Channel channel, Credit messagePayload) throws Exception {
        // Mendeklarasikan Exchange -> Tujuan : Memberitahu Broker ke Exchange mana
        // Message dikirimkan
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // Mendeklarasikan Queue -> Tujuan : Memberitahu broker ke Queue mana Message
        // dikirimkan
        channel.queueDeclare(QUEUE_NAMES[1], true, false, false, null);
        // Membuat Binding / Link
        channel.queueBind(QUEUE_NAMES[1], EXCHANGE_NAME, ROUTING_KEY[1]);

        // Publish Message
        // Konversi messagePayload -> JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonMessage = mapper.writeValueAsString(messagePayload);

        // Kirim ke Broker
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY[1], null, jsonMessage.getBytes());
        System.out.println("[>>] Message Credit Sudah Dikirim Ke Broker!");
    }
}
