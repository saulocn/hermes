package br.com.saulocn.hermes.mailer.kafka;

import br.com.saulocn.hermes.mailer.model.Message;
import br.com.saulocn.hermes.mailer.service.MessageService;
import org.eclipse.microprofile.reactive.messaging.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageProducer {

    @Inject
    private MessageService messageService;

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
                                null,
                                recipient
                        )));
    }

    @Incoming("mail-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public void receiveMail(String jsonMail) {
        System.out.println("Mail recebida: " + MailVO.fromJSON(jsonMail));
    }


}
