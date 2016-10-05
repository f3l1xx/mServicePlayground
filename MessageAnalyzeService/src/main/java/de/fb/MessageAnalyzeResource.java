package de.fb;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.common.util.concurrent.Uninterruptibles;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import de.fb.hystrix.CacheTestHyCommand;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;

@Path(MessageAnalyzeResource.MSG_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class MessageAnalyzeResource {

	static final String MSG_PATH = "msg-analyze";

	MessageAnalyzerService service = new MessageAnalyzerService();

	public MessageAnalyzeResource() {
	}

	@GET
	@Path("/{message}")
	public MessageAnalysis getUser(@PathParam("message") String msg, @Auth PrincipalImpl user) throws Exception {
		return service.analyze(msg);
	}

	
	@GET
	@Path("/ping/{name}")
	public String ping(@PathParam("name") String name) {

		try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {

			String result = "";
			for (int i = 0; i < 3; i++) {

				LambdaHystrixCommand<String> pingCmd = new LambdaHystrixCommand<>("PingCmd", "pingCache",
						() -> getPing(name), () -> "pong");

				Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);

				result = pingCmd.execute();
				System.out.println(i + ": " + result + " is from cache: " + pingCmd.isResponseFromCache());
			}
			return result;
		}
	}

	@GET
	@Path("/exception/{code}")
	public String exception(@PathParam("code") int code) {

		return new HystrixCommand<String>(
				HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Default"))
						.andCommandKey(HystrixCommandKey.Factory.asKey("exCmd"))) {

			@Override
			protected String run() throws Exception {
				return extracted(code);
			}

		}.execute();

		// LambdaHystrixCommand<String> exCmd = new
		// LambdaHystrixCommand<>("ExCmd", () -> extracted(code), null);
		// return exCmd.execute();
	}


	@GET
	@Path("/cache/{name}")
	public String testCache(@PathParam("name") String name) {
		try (HystrixRequestContext context = HystrixRequestContext.initializeContext()) {
			CacheTestHyCommand cacheTestHyCommand = new CacheTestHyCommand("key", name,
					HystrixCommandGroupKey.Factory.asKey("groupKey"));
			final AtomicReference<String> staticString = new AtomicReference<String>("");

			CacheTestHyCommand cacheTestHyCommand2 = new CacheTestHyCommand("key", name,
					HystrixCommandGroupKey.Factory.asKey("groupKey"));

			new Thread(new Runnable() {
				@Override
				public void run() {
					CacheTestHyCommand cacheTestHyCommand4 = new CacheTestHyCommand("key", name + "newThread",
							HystrixCommandGroupKey.Factory.asKey("groupKey"));
					staticString.set(cacheTestHyCommand4.execute());
				}
			}).run();

			CacheTestHyCommand cacheTestHyCommand3 = new CacheTestHyCommand("key2", name + "2",
					HystrixCommandGroupKey.Factory.asKey("groupKey"));

			Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
			
			return cacheTestHyCommand.execute() + "\n" + cacheTestHyCommand2.execute() + "\n"
					+ cacheTestHyCommand3.execute() + "\n" + staticString.get();
		}
	}

	
	private String extracted(int code) {
		switch (code) {
		case 404:
			throw new NotFoundException();

		default:
			throw new WebApplicationException(500);
		}
	}

	private String getPing(String name) {
		if (System.currentTimeMillis() % 8 == 0) {
			throw new RuntimeException();
		} else {
			return "ping" + name;
		}
	}

}
