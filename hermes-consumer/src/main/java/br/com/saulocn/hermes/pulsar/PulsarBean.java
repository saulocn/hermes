package br.com.saulocn.hermes.pulsar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.apache.pulsar.client.api.*;

import br.com.saulocn.hermes.service.MessageService;
import io.quarkus.runtime.annotations.QuarkusMain;

@ApplicationScoped
@QuarkusMain
public class PulsarBean {

	public static final String TOPIC_NAME = "message-topic";
	public static final String MAIL_NAME = "mail-topic";
	public static final String SUBSCRIPTION_NAME = "my-subscription";

	public static void main(String... args) throws PulsarClientException {
		Callable<Void> messageConsumer = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				messageTopicConsumer();
				return null;
			}
		};
		Callable<Void> mailConsumer = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				mailTopicConsumer();
				return null;
			}
		};

		List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
		taskList.add(messageConsumer);
		taskList.add(mailConsumer);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			executor.invokeAll(taskList);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	private static void mailTopicConsumer() throws PulsarClientException {
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		Consumer consumer = client.newConsumer().topic(MAIL_NAME).subscriptionName(SUBSCRIPTION_NAME)
				.ackTimeout(10, TimeUnit.SECONDS).subscriptionType(SubscriptionType.Exclusive).subscribe();
		while (true) {
			Messages<String> messages = consumer.batchReceive();
			messages.forEach(MessageService::process);
			consumer.acknowledge(messages);
		}
	}

	private static void messageTopicConsumer() throws PulsarClientException {
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		Consumer consumer = client.newConsumer().topic(TOPIC_NAME).subscriptionName(SUBSCRIPTION_NAME)
				.ackTimeout(10, TimeUnit.SECONDS).subscriptionType(SubscriptionType.Exclusive).subscribe();
		while (true) {
			Message msg = consumer.receive();
			try {
				sendToMailTopic();
				System.out.println(String.format("Message received on message: %s", new String(msg.getData())));
				consumer.acknowledge(msg);
			} catch (Exception e) {
				consumer.negativeAcknowledge(msg);
			}
		}
	}

	private static void sendToMailTopic() throws PulsarClientException {
		PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
		Producer<byte[]> producer = client.newProducer().topic(MAIL_NAME)
				.batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS).sendTimeout(10, TimeUnit.SECONDS)
				.blockIfQueueFull(true).create();
		producer.send("My message".getBytes());
		producer.close();
	}
}