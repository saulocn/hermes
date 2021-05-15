package br.com.saulocn.hermes.mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;

@ApplicationScoped
public class MailSenderService {

	@ConfigProperty(name = "quarkus.mailer.from")
	String mailFrom;

	@Inject
	ReactiveMailer reactiveMailer;

	public void sendMail() {
		reactiveMailer
				.send(Mail.withText(mailFrom, "A reactive email from quarkus", "This is my body")
						.addAttachment("my-file.txt", "content of my file".getBytes(), "text/plain"))
				.subscribe().with(success -> System.out.println("Sucesso"), error -> System.out.println("Erro"));
	}
}
