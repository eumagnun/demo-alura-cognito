package br.com.xptogames.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.cognitoidp.model.TooManyRequestsException;
import com.amazonaws.services.cognitoidp.model.UserType;

import br.com.xptogames.model.Usuario;
import br.com.xptogames.util.Config;

@Component
public class ListarUsuario extends AcaoUsuario{
	
	public List<Usuario> listAll() {
		List<Usuario> users = new ArrayList<Usuario>();

		try {
			ListUsersRequest listUserRequest = new ListUsersRequest()
					.withUserPoolId(Config.CognitoParameters.POOL_ID);
			ListUsersResult listUsersResult = cognitoClient.listUsers(listUserRequest);

			for (int x = 0; x < listUsersResult.getUsers().size(); x++) {
				UserType element = listUsersResult.getUsers().get(x);

				Usuario UsuarioAux = new Usuario();
				UsuarioAux.setCpf(element.getUsername());

				for (int i = 0; i < element.getAttributes().size(); i++) {
					if (element.getAttributes().get(i).getName().equalsIgnoreCase("name")) {
						UsuarioAux.setNome(element.getAttributes().get(i).getValue());
					} else if (element.getAttributes().get(i).getName().equalsIgnoreCase("phone_number")) {
						UsuarioAux.setTelefone(element.getAttributes().get(i).getValue());
					} else if (element.getAttributes().get(i).getName().equalsIgnoreCase("email")) {
						UsuarioAux.setEmail(element.getAttributes().get(i).getValue());
					}
				}
				users.add(UsuarioAux);
			}

		} catch (InvalidParameterException ex) {
			ex.printStackTrace();
		} catch (ResourceNotFoundException ex) {
			ex.printStackTrace();
		} catch (TooManyRequestsException ex) {
			ex.printStackTrace();
		}
		return users;
	}


}
