package de.fb;

import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl> {
	@Override
	public Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException {
		if ("secret".equals(credentials.getPassword())) {
			return Optional.of(new PrincipalImpl(credentials.getUsername()));
		}
		return Optional.absent();
	}
}