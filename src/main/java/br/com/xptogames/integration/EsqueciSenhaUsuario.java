package br.com.xptogames.integration;

import javax.jws.WebParam;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.util.Config;

@Component
public class EsqueciSenhaUsuario extends AcaoUsuario {

	public UserWSResponse forgotPassword(@WebParam(name = "username") String username) {
		try {
			userWSResponse = new UserWSResponse();

			ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
			forgotPasswordRequest.withClientId(Config.CognitoParameters.CLIENT_ID);
			forgotPasswordRequest.withUsername(username);
			cognitoClient.forgotPassword(forgotPasswordRequest);

			userWSResponse.setStatus(1);
			userWSResponse.setMessage("Codigo de acesso enviado por e-mail");

			return userWSResponse;

		} catch (Exception ex) {
			userWSResponse.setStatus(2);
			userWSResponse.setMessage("Parametro invalido");
			ex.printStackTrace();
		}

		return userWSResponse;
	}

}
