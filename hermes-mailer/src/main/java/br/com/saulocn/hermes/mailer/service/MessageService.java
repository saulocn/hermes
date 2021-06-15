package br.com.saulocn.hermes.mailer.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;

import br.com.saulocn.hermes.mailer.model.Message;

@ApplicationScoped
public class MessageService {

	@Inject
	MongoClient mongoClient;

	public List<Message> list(){
		List<Message> messages = new ArrayList<>();
		MongoCursor<Document> cursor = getCollection().find().iterator();

		try{
			while(cursor.hasNext()){
				Document document = cursor.next();
				
				Message message = new Message();
				message.setTitle(document.getString("title"));
				message.setText(document.getString("text"));

				messages.add(message);
			}
		} finally {
			cursor.close();
		}

		return messages;
	}


    private MongoCollection getCollection(){
        return mongoClient.getDatabase("hermes").getCollection("messages");
    }
}
