package de.fb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(WordCountResource.COUNT_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class WordCountResource   {

	static final String COUNT_PATH = "count";
	
	public WordCountResource() {
	}
	
	
	@GET
	@Path("/{message}")
	public int getCount(@PathParam("message") String msg) throws Exception{
		return msg.split(" ").length ;
	}
	
}
