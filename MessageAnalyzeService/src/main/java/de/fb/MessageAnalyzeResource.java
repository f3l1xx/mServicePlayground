package de.fb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;


@Path(MessageAnalyzeResource.USERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class MessageAnalyzeResource   {

	static final String USERS_PATH = "msg-analyze";
	
	MessageAnalyzerService service = new MessageAnalyzerService();
	
	public MessageAnalyzeResource() {
	}
	
	
	@GET
	@Path("/{message}")
	public MessageAnalysis getUser(@PathParam("message") String msg, @Auth PrincipalImpl user) throws Exception{
		return service.analyze(msg);
	}
	
}
