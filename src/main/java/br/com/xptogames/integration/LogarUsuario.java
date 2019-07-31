package br.com.xptogames.integration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.TooManyRequestsException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.util.StringUtils;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

@Component
public class LogarUsuario extends AcaoUsuario {

	public UserWSResponse login(Usuario user) {
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

				String jws = authResponse.getAuthenticationResult().getIdToken();

				int i = jws.lastIndexOf('.');
				String withoutSignature = jws.substring(0, i + 1);
				Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

				String userGroups = ((Claims) untrusted.getBody()).get("cognito:groups") + "";

				userWSResponse.setStatus(1);
				userWSResponse.setMessage("Login realizado com sucesso");
				userWSResponse.setToken(authResponse.getAuthenticationResult().getIdToken());

				return userWSResponse;
			} else {
				userWSResponse.setStatus(0);
				userWSResponse.setMessage("Nao foi possivel realizar login");
				return userWSResponse;
			}

		} catch (UserNotFoundException ex) {
			userWSResponse.setStatus(2);
			userWSResponse.setMessage("Usuario nao encontrado");
			ex.printStackTrace();
		} catch (NotAuthorizedException ex) {
			userWSResponse.setStatus(3);
			userWSResponse.setMessage("Login e senha invalidos");
			ex.printStackTrace();
		} catch (TooManyRequestsException ex) {
			userWSResponse.setStatus(4);
			userWSResponse.setMessage("Tente novamente mais tarde");
			ex.printStackTrace();
		}

		return userWSResponse;
	}
}
