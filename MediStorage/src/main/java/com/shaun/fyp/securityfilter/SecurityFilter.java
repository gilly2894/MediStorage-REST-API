package com.shaun.fyp.securityfilter;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import com.shaun.fyp.model.User;
import com.shaun.fyp.service.UserService;

/*
 * This class is the security filter.
 * It catches all requests into the Patient Resource Class and tries to authenticate the user first.
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

	private static final String AUTH_KEY = "Authorization";
	private static final String AUTH_PREFIX = "Basic ";
	private static final String PATIENT_PREFIX = "patients";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if(requestContext.getUriInfo().getPath().contains(PATIENT_PREFIX))	
		{
			List<String> authHeader = requestContext.getHeaders().get(AUTH_KEY);
			if(authHeader != null && authHeader.size() > 0)
			{
				String authToken = authHeader.get(0);
				authToken = authToken.replaceFirst(AUTH_PREFIX, "");
				String decodedString = Base64.decodeAsString(authToken);
				
				String[] temp = decodedString.split(":");
				String username = temp[0];
				String password = temp[1];
				
				UserService userService = new UserService();
				User user = userService.getUserByUserName(username);
				if(user!=null && password.equals(user.getPassword()))
					return;
			}
			Response unauthorizedStatus = Response
												.status(Response.Status.UNAUTHORIZED)
												.entity("User cannot access the resource.")
												.build();
			requestContext.abortWith(unauthorizedStatus);
		}
	}

}
