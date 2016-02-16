package de.fb;

import java.util.function.Supplier;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class LambdaHystrixCommand<Out> extends HystrixCommand<Out> {

	private Supplier<Out> runSupplier;
	private Supplier<Out> fallbackSupplier;

	public LambdaHystrixCommand(
            Supplier<Out> runSupplierIn,
            Supplier<Out> fallbackSupplierIn) {
        super(HystrixCommandGroupKey.Factory.asKey("DefaultGroup"));

        runSupplier = runSupplierIn;
        fallbackSupplier = fallbackSupplierIn;
    }

	@Override
	protected Out run() throws Exception {
		return runSupplier.get();
	}

	@Override
	protected Out getFallback() {
		return fallbackSupplier.get();
	}
}