package br.com.saulocn.hermes.mailer.kafka;

import br.com.saulocn.hermes.mailer.model.Message;
import br.com.saulocn.hermes.mailer.service.MessageService;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageProducer {

    @Inject
    private MessageService messageService;

    @Incoming("init")
    @Outgoing("messages-out")
    public String sendMessage(String message){
        return message;
    }


    @Incoming("messages-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public MailVO receiveMessage(String incomingMessage){
        Message message = messageService.findById(incomingMessage);
        System.out.println("Mensagem recebida: "+ message);
        return new MailVO();
    }


}
