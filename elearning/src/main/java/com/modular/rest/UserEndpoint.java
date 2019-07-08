package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserEndpoint {
    private static final Logger logger = Logger.getLogger(UserEndpoint.class);
    private Gson gson = new Gson();
    private UserDAO userDAO = new UserDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String json){
        try{
            User user = gson.fromJson(json, User.class);
            userDAO.create(user);
            return Response.ok().build();
        }
        catch(JsonSyntaxException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") int userId){
        try{
            User user = userDAO.get(userId);
            String userJson = gson.toJson(user);
            return Response.ok(userJson).build();
        }
        catch (DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") int userId, String json){
        try{
            User user = gson.fromJson(json, User.class);
            userDAO.update(user);
            return Response.ok().build();
        }
        catch(JsonSyntaxException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
        }
        catch (DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") int userId){
        try{
            User user = userDAO.get(userId);
            userDAO.delete(user);
            return Response.ok().build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    public Response getAllUser(){
        try{
            List<User> users = userDAO.getAllUsers();
            return Response.ok().build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }
}
