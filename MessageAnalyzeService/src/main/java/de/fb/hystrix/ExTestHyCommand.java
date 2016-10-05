package de.fb.hystrix;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.WebApplicationException;

import com.google.common.util.concurrent.Uninterruptibles;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;

public class ExTestHyCommand extends HystrixCommand<String> {

	private static boolean shouldFail;

	protected ExTestHyCommand(HystrixCommandGroupKey group) {
		super(group);
	}

	@Override
	protected String run() throws Exception {
		return OtherClass.foo();
	}

	public static class OtherClass {
		public static String foo() throws WebApplicationException {
			if (shouldFail) {
				throw new HystrixBadRequestException("bad data");
				//throw new WebApplicationException("should fail", 433);
			}
			Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
			return "foo";
		}
	}

	@Override
	protected String getFallback() {
		Throwable ex = getExecutionException();
		if (ex != null) {
			System.out.println("Fehler: " + ex);
			throw new WebApplicationException(477);
		}
		return super.getFallback();
	}

	public void fail(boolean shouldFail) {
		ExTestHyCommand.shouldFail = shouldFail;
	}

}
