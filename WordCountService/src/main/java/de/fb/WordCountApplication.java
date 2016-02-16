package de.fb;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WordCountApplication extends Application<WordCountConfig> {


	public static void main(String[] args) throws Exception {
		new WordCountApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<WordCountConfig> bootstrap) {
		super.initialize(bootstrap);
	}
	
	@Override
	public void run(WordCountConfig conf, Environment env) throws Exception {
		WordCountResource resource = new WordCountResource();
		env.jersey().register(resource);
	}


}
