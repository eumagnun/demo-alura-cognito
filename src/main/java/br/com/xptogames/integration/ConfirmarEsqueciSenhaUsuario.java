package br.com.xptogames.integration;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;

@Component
public class ConfirmarEsqueciSenhaUsuario extends AcaoUsuario {

	public UserWSResponse confirmForgotPassword(Usuario usuario) {
		try {
			userWSResponse = new UserWSResponse();

			ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest();
			confirmForgotPasswordRequest.setClientId(Config.CognitoParameters.CLIENT_ID);
			confirmForgotPasswordRequest.setConfirmationCode(usuario.getCodigoConfirmacao());
			confirmForgotPasswordRequest.setPassword(usuario.getSenha());
			confirmForgotPasswordRequest.setUsername(usuario.getCpf());

			cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);

			userWSResponse.setStatus(1);
			userWSResponse.setMessage("Senha alterada com sucesso");

			return userWSResponse;
		} catch (InvalidPasswordException ex) {
			userWSResponse.setStatus(2);
			userWSResponse.setMessage("Senha nao esta de acordo com a politica");
			ex.printStackTrace();
		} catch (Exception ex) {
			userWSResponse.setStatus(3);
			userWSResponse.setMessage("Erro ao alterar senha");
			ex.printStackTrace();
		}

		return userWSResponse;
	}

}
