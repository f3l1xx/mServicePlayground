package de.fb;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MessageAnalyzeApplication extends Application<MessageAnalyzeConfig> {


	public static void main(String[] args) throws Exception {
		
		new MessageAnalyzeApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<MessageAnalyzeConfig> bootstrap) {
		super.initialize(bootstrap);
	}
	
	@Override
	public void run(MessageAnalyzeConfig conf, Environment env) throws Exception {
		
		MessageAnalyzeResource resource = new MessageAnalyzeResource();
		
		env.jersey().register(resource);

	
	    env.jersey().register(new AuthDynamicFeature(
	            new BasicCredentialAuthFilter.Builder<PrincipalImpl>()
	                .setAuthenticator(new SimpleAuthenticator())
	                .buildAuthFilter()));
	    //If you want to use @Auth to inject a custom Principal type into your resource
	    env.jersey().register(new AuthValueFactoryProvider.Binder<>(PrincipalImpl.class));
	
	}


}
