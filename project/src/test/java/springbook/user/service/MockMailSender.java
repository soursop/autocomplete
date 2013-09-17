package springbook.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;


public class MockMailSender implements MailSender {
	
	private List<String> requests = new ArrayList<String>();

	@Override
	public void send(SimpleMailMessage msg) throws MailException {
		requests.add(msg.getTo()[0]);
	}

	@Override
	public void send(SimpleMailMessage[] msg) throws MailException {
	}

	public List<String> getRequests() {
		return this.requests;
	}

}
