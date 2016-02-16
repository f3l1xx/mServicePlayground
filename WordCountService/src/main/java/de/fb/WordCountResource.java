package de.fb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(WordCountResource.USERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class WordCountResource   {

	static final String USERS_PATH = "count";
	
	public WordCountResource() {
	}
	
	
	@GET
	@Path("/{message}")
	public int getUser(@PathParam("message") String msg) throws Exception{
		return msg.split(" ").length ;
	}
	
}
