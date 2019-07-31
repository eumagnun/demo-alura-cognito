package br.com.xptogames.integration;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;

import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.util.Config;

public class AcaoUsuario {
	protected UserWSResponse userWSResponse;
	protected AWSCredentials awsCredentials = new BasicAWSCredentials(Config.AWS.AWS_ACCESS_KEY_ID,
			Config.AWS.AWS_SECRET_ACCESS_KEY);
	protected AWSCognitoIdentityProviderClient cognitoClient = new AWSCognitoIdentityProviderClient(awsCredentials);

}
