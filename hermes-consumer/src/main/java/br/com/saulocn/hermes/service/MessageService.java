package br.com.saulocn.hermes.service;

import org.apache.pulsar.client.api.Message;

public class MessageService {

    public static void process(Message message) {
        System.out.printf("Message received: %s", new String(message.getData()));
    }
}
