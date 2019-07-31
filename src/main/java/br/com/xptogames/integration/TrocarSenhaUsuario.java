package br.com.xptogames.integration;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebParam;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.util.StringUtils;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;

@Component
public class TrocarSenhaUsuario extends AcaoUsuario {

	public UserWSResponse changePassword(Usuario user, @WebParam(name = "newPassword") String newPassword) {
		try {
			userWSResponse = new UserWSResponse();
			Map<String, String> authParams = new HashMap<String, String>();
			authParams.put("USERNAME", user.getCpf());
			authParams.put("PASSWORD", user.getSenha());

			AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
					.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withAuthParameters(authParams)
					.withClientId(Config.CognitoParameters.CLIENT_ID)
					.withUserPoolId(Config.CognitoParameters.POOL_ID);

			AdminInitiateAuthResult authResponse = cognitoClient.adminInitiateAuth(authRequest);

			if (StringUtils.isNullOrEmpty(authResponse.getChallengeName())) {
				userWSResponse.setToken(authResponse.getAuthenticationResult().getIdToken());

				ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest()
						.withAccessToken(authResponse.getAuthenticationResult().getAccessToken());
				changePasswordRequest.setPreviousPassword(user.getSenha());
				changePasswordRequest.setProposedPassword(newPassword);

				new GetUserRequest().withAccessToken(authResponse.getAuthenticationResult().getAccessToken());
				cognitoClient.changePassword(changePasswordRequest);

				userWSResponse.setStatus(1);
				userWSResponse.setMessage("Senha alterada com sucesso");

				return userWSResponse;
			} else {
				userWSResponse.setStatus(0);
				userWSResponse.setMessage("Nao foi possivel realizar login");
				return userWSResponse;
			}

		} catch (Exception ex) {
			userWSResponse.setStatus(2);
			userWSResponse.setMessage("Parametro invalido");
			ex.printStackTrace();
		}

		return userWSResponse;
	}

}
