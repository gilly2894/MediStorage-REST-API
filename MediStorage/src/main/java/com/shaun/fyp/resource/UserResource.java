package com.shaun.fyp.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.shaun.fyp.model.User;
import com.shaun.fyp.service.UserService;

/*
 * This is the User Resource it is used to do execute the HTTP Methods related to the user.
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
	UserService userService = new UserService();
	
	/*
	 * This is a GET Method to get the user with the userName specified in the Path
	 */
	@GET
	@Path("/{userName}")
	public Response getPatientById(@PathParam("userName") String userName, @Context UriInfo uriInfo)
	{
		User theUser = userService.getUserByUserName(userName);
		if(theUser != null) {
			return Response.ok()
					.entity(theUser)
					.build();
		}
		else 
			return Response.status(Status.NOT_FOUND).build();
	}
	
	/*
	 * This is a POST Method that creates a new user
	 * The user parameter is the message body of the request
	 * Jersey does this conversion from JSON to a User
	 */
	@POST
	public Response addUser(User user, @Context UriInfo uriInfo)
	{
		User newUser = userService.addNewUser(user);
		if(newUser != null)
		{
			return Response.status(Status.CREATED)
					.entity(newUser)
					.build();
		}
		else
			return Response.status(Status.CONFLICT).build();
	}
}
