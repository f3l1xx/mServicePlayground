package de.fb;

import java.util.function.Supplier;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandKey;

import de.fb.hystrix.CmdGroup;

public class LambdaHystrixCommand<T> extends HystrixCommand<T> {

	private static final com.netflix.hystrix.HystrixCommand.Setter CMD_SETTER_WITH_DEFAULT_GROUP = HystrixCommand.Setter.withGroupKey(CmdGroup.DEFAULT);
	
	private Supplier<T> runSupplier;
	private Supplier<T> fallbackSupplier;

	private String cacheKey;

	
	public LambdaHystrixCommand(String commandkey, String cacheKey, Supplier<T> runSupplier, Supplier<T> fallbackSupplier) {
		super(CMD_SETTER_WITH_DEFAULT_GROUP
				.andCommandKey(HystrixCommandKey.Factory.asKey(commandkey)));
		this.cacheKey = cacheKey;

		this.runSupplier = runSupplier;
		this.fallbackSupplier = fallbackSupplier;
	}
	
	public LambdaHystrixCommand(String commandkey, Supplier<T> runSupplier, Supplier<T> fallbackSupplier) {
		this(commandkey,null,runSupplier,fallbackSupplier);
	}


	@Override
	protected String getCacheKey() {
		return cacheKey;
	}
	
	@Override
	protected T run() throws Exception {
		return runSupplier.get();
	}

	@Override
	protected T getFallback() {
		if(fallbackSupplier == null){
			throw new UnsupportedOperationException();
		}
		return fallbackSupplier.get();
	}
	
}