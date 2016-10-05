package de.fb;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl> {

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException {
		
		System.out.println("Asking for authentication: " + credentials);
		
		// dummy ( and stupid) implementation of an authenticator
		// 1. check that user service knows that email address
		LambdaHystrixCommand<Boolean> cmdUserExists = new LambdaHystrixCommand<>("checkUserExists",
				() -> checkUserExists(credentials.getUsername()), () -> Boolean.FALSE);
		
		
		if (cmdUserExists.execute()) {
			System.out.println("User exists.");
			// 2. check that basic auth password is secret
			if ("secret".equals(credentials.getPassword())) {
				return Optional.of(new PrincipalImpl(credentials.getUsername()));
			}
		}

		System.out.println("User is unkown. isHystrixFallback: " + cmdUserExists.isResponseFromFallback());

		return Optional.absent();
	}

	private Boolean checkUserExists(String mail) {
		try {
			URL url = new URL("http://user-auth:8065/users/" + MessageAnalyzerService.escape(mail));
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			return http.getResponseCode() == 200;

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}