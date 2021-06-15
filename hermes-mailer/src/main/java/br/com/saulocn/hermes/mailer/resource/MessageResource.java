package br.com.saulocn.hermes.mailer.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.saulocn.hermes.mailer.model.Message;
import br.com.saulocn.hermes.mailer.service.MessageService;

@Path("/messages")
public class MessageResource {

    @Inject
    MessageService messageService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> list() {
        return messageService.list();
    }

}
