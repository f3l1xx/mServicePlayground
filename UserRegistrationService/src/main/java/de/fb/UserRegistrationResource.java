package de.fb;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;


@Path(UserRegistrationResource.USERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistrationResource   {

	static final String USERS_PATH = "users";
	Map<UUID, User> userStore;
	
	public UserRegistrationResource(Map<UUID,User> userStore) {
		this.userStore = userStore;
	}
	
	@GET
	public List<User> getUsers(){
		return Lists.newArrayList(userStore.values());
	}
	
	@GET
	@Path("/{id}")
	public Optional<User> getUser(@PathParam("id") UUID id){
		return Optional.fromNullable(userStore.get(id));
	}
	
	@POST
	public Response addUser(@Valid User user) throws Exception{
		user.setId(UUID.randomUUID());
		userStore.put(user.getId(), user);
		
		return Response.created(new URI(USERS_PATH + "/" + user.getId().toString())).build();
	}
}
