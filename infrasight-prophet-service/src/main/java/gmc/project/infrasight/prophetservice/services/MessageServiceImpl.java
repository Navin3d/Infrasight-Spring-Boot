package gmc.project.infrasight.prophetservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private Environment environment;

	@Override
	public void sendMessage(String to, String body) {
		Twilio.init(environment.getProperty("twilloAuthToken"), environment.getProperty("twillioPassword"));

		Message.creator(new PhoneNumber(to),
				new PhoneNumber(environment.getProperty("twilioPhoneNumber")), body).create();

	}

}
