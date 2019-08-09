package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.ChatDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.dao.impl.ChatDAOImpl;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.Chat;
import com.modular.persistence.model.Conversation;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.List;

@Path("/user")
public class UserEndpoint {
    private static final Logger logger = Logger.getLogger(UserEndpoint.class);
    private Gson gson = new Gson();
    private UserDAO userDAO = new UserDAOImpl();
    private ChatDAO chatDAO = new ChatDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String json){
        try{
            User user = gson.fromJson(json, User.class);
            userDAO.create(user);
            return Response.ok("Success").build();
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
            user.setIdUser(userId);
            userDAO.update(user);
            return Response.ok("Success").build();
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
            return Response.ok("Success").build();
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
            String usersJson = gson.toJson(users);
            return Response.ok(usersJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @POST
    @Path("{sender}/chat/{receiver}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMessage(@PathParam("sender") int senderId, @PathParam("receiver") int receiverId, String json){
        try{
            Chat message = new Chat();
            User sender = userDAO.get(senderId);
            User receiver = userDAO.get(receiverId);
            JsonObject messageJson = gson.fromJson(json, JsonObject.class);
            message.setIdUser(sender);
            message.setIdUser1(receiver);
            message.setMessage(messageJson.get("message").getAsString());
            message.setDate(Calendar.getInstance().getTime());
            chatDAO.create(message);
            return Response.ok("Success").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{sender}/chat/{receiver}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConversation(@PathParam("sender") int senderID, @PathParam("receiver") int receiverId){
        try{
            List<Chat> chats = chatDAO.getConversation(senderID, receiverId);
            Conversation conversation = Conversation.createConversation(chats);
            String json = gson.toJson(conversation);
            return Response.ok(json).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }
}
