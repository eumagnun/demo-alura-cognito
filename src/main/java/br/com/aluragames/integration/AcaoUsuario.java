package br.com.aluragames.integration;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;

import br.com.aluragames.model.DefaultResponse;
import br.com.aluragames.util.Config;

public class AcaoUsuario {
	protected DefaultResponse defaultResponse;
	protected AWSCredentials awsCredentials = new BasicAWSCredentials(Config.AWS.AWS_ACCESS_KEY_ID,
			Config.AWS.AWS_SECRET_ACCESS_KEY);
	protected AWSCognitoIdentityProviderClient cognitoClient = new AWSCognitoIdentityProviderClient(awsCredentials);

}
