package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.*;
import com.modular.persistence.dao.impl.ChatDAOImpl;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.*;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Set;

@Path("/user")
public class UserEndpoint {
    private static final Logger logger = Logger.getLogger(UserEndpoint.class);
    private Gson gson = new Gson();
    private UserDAO userDAO = new UserDAOImpl();
    private ChatDAO chatDAO = new ChatDAOImpl();
    private CourseDAO courseDAO = new CourseDAOImpl();

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
            for(Course course : user.getCourses()){
                course.setUsers(null);
            }
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
            Set<User> users = userDAO.getAllUsers();
            String usersJson = gson.toJson(users);
            return Response.ok(usersJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }
    @PUT
    @Path("{userId}/enroll/{courseId}")
    public Response enrollCourse(@PathParam("userId") int userId,
                                 @PathParam("courseId") int courseId,
                                 String passwordJson)
    {
        try{
            Course course = courseDAO.get(courseId);
            User user = userDAO.get(userId);
            if(user.getUserType().getIdUserType() == UserType.TEACHER){
                Response.status(403).entity("Un profesor no puede registrarse en un curso").build();
            }
            JsonObject password = gson.fromJson(passwordJson, JsonObject.class);
            userDAO.enrollCourse(course, user, password.get("password").getAsString());
            return Response.ok("Success").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity("No fue posible hacer el registro").build();
        }
        catch(IncorrectPasswordException ipe){
            logger.debug(ipe.getMessage(), ipe);
            return Response.status(400).entity(ipe.getMessage()).build();
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
            Set<Chat> chats = chatDAO.getConversation(senderID, receiverId);
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
