package gmc.project.infrasight.prophetservice.services;

import gmc.project.infrasight.prophetservice.models.MailingModel;

public interface MailingService {
	public void sendMail(MailingModel mailingModel);
}
