package br.com.saulocn.hermes.mailer.kafka;

import br.com.saulocn.hermes.mailer.model.Message;
import br.com.saulocn.hermes.mailer.service.MailSenderService;
import br.com.saulocn.hermes.mailer.service.MessageService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.groups.UniSubscribe;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class MessageProducer {

    AtomicInteger counter = new AtomicInteger();

    @ConfigProperty(name = "quarkus.mailer.from")
    String mailFrom;

    @Inject
    ReactiveMailer reactiveMailer;
    @Inject
    private MessageService messageService;

    @Inject
    private MailSenderService mailSenderService;

    @Inject
    @Channel("mailer")
    Emitter<MailVO> emitter;

    @Incoming("init")
    @Outgoing("messages-out")
    public String sendMessage(String message) {
        return message;
    }

    @Incoming("mailer")
    @Outgoing("mail-out")
    public String sendMessage(MailVO mailVO) {
        System.out.println("Enviando mailVO: " + mailVO);
        return mailVO.toJSON();
    }

    @Incoming("messages-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveMessage(String incomingMessage) {
        Message message = messageService.findById(incomingMessage);
        System.out.println("Mensagem recebida: " + message);
        message.getRecipients().forEach(
                recipient -> emitter.send(
                        MailVO.of(message.getTitle(),
                                message.getText(),
                                message.getContentType(),
                                recipient
                        )));
    }

    @Incoming("mail-in")
    @Blocking
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public CompletableFuture<Void> receiveMail(org.eclipse.microprofile.reactive.messaging.Message<String> jsonMail) {
        MailVO mailVO = MailVO.fromJSON(jsonMail.getPayload());
        System.out.println("Mail recebida: " + mailVO.toJSON());

        System.out.println("Sending email:" + mailVO);
        UniSubscribe<Void> subscribe = reactiveMailer
                .send(Mail.withHtml(mailFrom, mailVO.getSubject(), mailVO.getText())
                        .addTo(mailVO.getTo())
                )
                .subscribe();
        subscribe.with(success -> {
                    System.out.println("Mail sent! " + counter.incrementAndGet());
                },
                error -> {
                    System.out.println("error! " + error.getMessage());
                });
        return subscribe.asCompletionStage();
    }


}
