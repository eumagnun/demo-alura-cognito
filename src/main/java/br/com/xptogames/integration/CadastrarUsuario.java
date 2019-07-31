package br.com.xptogames.integration;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;


@Component
public class CadastrarUsuario extends AcaoUsuario{

	
	public UserWSResponse create(Usuario user) {
		userWSResponse = new UserWSResponse();

		AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
				.withUserPoolId(Config.CognitoParameters.POOL_ID).withUsername(user.getCpf())
				.withUserAttributes(
						new AttributeType().withName("email").withValue(user.getEmail()),
						new AttributeType().withName("name").withValue(user.getNome()),
						new AttributeType().withName("phone_number").withValue("+55"+user.getTelefone()),
						new AttributeType().withName("email_verified").withValue("true"))
				.withDesiredDeliveryMediums(DeliveryMediumType.EMAIL).withForceAliasCreation(Boolean.FALSE);

		AdminCreateUserResult adminCreateUserResult = cognitoClient.adminCreateUser(cognitoRequest);

		if (adminCreateUserResult.getUser() != null) {
			userWSResponse.setStatus(1);
			userWSResponse.setMessage("Usuario criado com sucesso");
			return userWSResponse;
		} else {
			userWSResponse.setStatus(0);
			userWSResponse.setMessage("Nao foi possivel criar usuario");
			return userWSResponse;
		}
	}

}
