package br.com.aluragames.integration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;

import br.com.aluragames.model.DefaultResponse;
import br.com.aluragames.model.Usuario;
import br.com.aluragames.util.Config;

@Component
public class LogarUsuario extends AcaoUsuario {

	public DefaultResponse execute(Usuario user) {
		defaultResponse = new DefaultResponse();

		
		return defaultResponse;

	}
}
