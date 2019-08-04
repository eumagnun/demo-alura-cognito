package br.com.aluragames.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.UserType;

import br.com.aluragames.model.DefaultResponse;
import br.com.aluragames.model.Usuario;
import br.com.aluragames.util.Config;

@Component
public class ListarUsuario extends AcaoUsuario {

	public List<Usuario> execute() {
		defaultResponse = new DefaultResponse();
		List<Usuario> users = new ArrayList<Usuario>();

		
		return users;
	}

}
