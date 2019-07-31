package br.com.xptogames.integration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.TooManyRequestsException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;

@Component
public class ConfirmarCadastroUsuario extends AcaoUsuario {

	public UserWSResponse confirmSignUp(Usuario user) {
		userWSResponse = new UserWSResponse();

		try {

			Map<String, String> initialParams = new HashMap<String, String>();
			initialParams.put("USERNAME", user.getCpf());
			initialParams.put("PASSWORD", user.getCodigoConfirmacao());

			AdminInitiateAuthRequest initialRequest = new AdminInitiateAuthRequest()
					.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(initialParams)
					.withClientId(Config.CognitoParameters.CLIENT_ID)
					.withUserPoolId(Config.CognitoParameters.POOL_ID);

			AdminInitiateAuthResult initialResponse = cognitoClient.adminInitiateAuth(initialRequest);

			if (!"NEW_PASSWORD_REQUIRED".equalsIgnoreCase(initialResponse.getChallengeName())) {
				throw new RuntimeException("unexpected challenge: " + initialResponse.getChallengeName());
			}

			Map<String, String> challengeResponses = new HashMap<String, String>();
			challengeResponses.put("USERNAME", user.getCpf());
			challengeResponses.put("PASSWORD", user.getCodigoConfirmacao());
			challengeResponses.put("NEW_PASSWORD", user.getSenha());
			if (user.getTelefone() != null) {
				challengeResponses.put("userAttributes.phone_number", "+" + user.getTelefone());
			}
			challengeResponses.put("userAttributes.name", user.getNome());

			new AdminRespondToAuthChallengeRequest().withChallengeName("NEW_PASSWORD_REQUIRED")
					.withChallengeResponses(challengeResponses).withClientId(Config.CognitoParameters.CLIENT_ID)
					.withUserPoolId(Config.CognitoParameters.POOL_ID).withSession(initialResponse.getSession());

			userWSResponse.setStatus(1);
			userWSResponse.setMessage("Usuario confirmado com sucesso");
		} catch (InvalidPasswordException ex) {
			userWSResponse.setStatus(2);
			userWSResponse.setMessage("Senha invalida");
			ex.printStackTrace();
		} catch (UserNotFoundException ex) {
			userWSResponse.setStatus(3);
			userWSResponse.setMessage("Usuario nao encontrado");
			ex.printStackTrace();
		} catch (NotAuthorizedException ex) {
			userWSResponse.setStatus(4);
			userWSResponse.setMessage("Usuario nao autorizado");
			ex.printStackTrace();
		} catch (TooManyRequestsException ex) {
			userWSResponse.setStatus(5);
			userWSResponse.setMessage("tente novamente mais tarde");
			ex.printStackTrace();
		}

		return userWSResponse;
	}

}
