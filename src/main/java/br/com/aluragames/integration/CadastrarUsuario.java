package br.com.aluragames.integration;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;

import br.com.aluragames.model.DefaultResponse;
import br.com.aluragames.model.Usuario;
import br.com.aluragames.util.Config;

@Component
public class CadastrarUsuario extends AcaoUsuario {

	public DefaultResponse execute(Usuario user) {
		defaultResponse = new DefaultResponse();

	
		return defaultResponse;

	}

}
