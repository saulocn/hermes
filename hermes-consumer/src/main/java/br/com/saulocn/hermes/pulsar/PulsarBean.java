package br.com.saulocn.hermes.pulsar;


import br.com.saulocn.hermes.service.MessageService;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.pulsar.client.api.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@QuarkusMain
public class PulsarBean {

    public static final String TOPIC_NAME = "message-topic";
    public static final String SUBSCRIPTION_NAME = "my-subscription";

    public static void main(String... args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
        .serviceUrl("pulsar://localhost:6650")
        .build();
        Consumer consumer = client.newConsumer()
                .topic(TOPIC_NAME)
                .subscriptionName(SUBSCRIPTION_NAME)
                .ackTimeout(10, TimeUnit.SECONDS)
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();
        while(true) {
            Messages<String> messages = consumer.batchReceive();
            messages.forEach(MessageService::process);
            consumer.acknowledge(messages);
        }
        /*while (true) {
            Message msg = consumer.receive();
            try {
                System.out.printf("Message received: %s", new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
            }
        }*/

    }
}