package br.com.saulocn.hermes.mailer.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.saulocn.hermes.mailer.kafka.MailVO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;

@ApplicationScoped
public class MailSenderService {

	@ConfigProperty(name = "quarkus.mailer.from")
	String mailFrom;

	@Inject
	ReactiveMailer reactiveMailer;

	public void sendMail(MailVO mailVO) {
		System.out.println("Sending email:" + mailVO);
		reactiveMailer
				.send(Mail.withText(mailFrom, mailVO.getSubject(), mailVO.getText())
						.addTo(mailVO.getTo())
						//.addAttachment("my-file.txt", "content of my file".getBytes(), "text/plain")
				)
				.subscribe().with(success -> System.out.println("Sucesso"), error -> System.out.println("Erro"));
	}
}
