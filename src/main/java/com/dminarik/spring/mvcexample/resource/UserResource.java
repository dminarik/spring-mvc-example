package com.dminarik.spring.mvcexample.resource;

import com.dminarik.spring.mvcexample.data.UserData;
import com.dminarik.spring.mvcexample.service.user.UserAlreadyExistsException;
import com.dminarik.spring.mvcexample.service.user.UserNotFoundException;
import com.dminarik.spring.mvcexample.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/user")
@Service
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Consumes("application/json")
    public Response createUser(UserData user) {
        try {
            userService.createUser(user);
            return Response.status(Response.Status.CREATED).build();
        } catch (UserAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/{username}")
    @Produces("application/json")
    public Response getUser(@PathParam("username") String username) {
        try {
            return Response.ok(userService.findUser(username)).build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{username}")
    public Response deleteUser(@PathParam("username") String username) {
        try {
            userService.deleteUser(username);
            return Response.ok().build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
