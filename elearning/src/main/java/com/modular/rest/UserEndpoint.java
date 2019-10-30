package com.modular.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.modular.fuzzy.FuzzyLearning;
import com.modular.persistence.dao.*;
import com.modular.persistence.dao.impl.ChatDAOImpl;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.*;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Path("/user")
public class UserEndpoint {
    private static final Logger logger = Logger.getLogger(UserEndpoint.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private Gson gson = new Gson();

    @Inject
    private UserDAO userDAO ;
    @Inject
    private ChatDAO chatDAO;
    @Inject
    private CourseDAO courseDAO;
    @Inject
    private QuestionDAO questionDAO;

    static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user){
        try{
            logger.debug("Request json: " + user);
            //User user = mapper.readValue(json, User.class);
            userDAO.create(user);
            return Response.ok(200).build();
        } catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        } catch (Exception io) {
            logger.debug(io.getMessage(), io);
            return Response.serverError().entity(io.getMessage()).build();
        }
    }

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") int userId){
        try{
            User user = userDAO.get(userId);
            //String userJson = mapper.writeValueAsString(user);
            return Response.ok(user).build();
        }
        catch (DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") int userId, User user){
        try{
            //User user = mapper.readValue(json, User.class);
            user.setIdUser(userId);
            userDAO.update(user);
            return Response.ok("Success").build();
        } catch (DataBaseException dbe){
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
            return Response.ok(users).build();
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
                return Response.status(403).entity("Un profesor no puede registrarse en un curso").build();
            }
            if(user.getCourses().contains(course)) {
                return Response.status(400).entity("El usuario ya se encuentra inscrito en el curso").build();
            }
            JsonObject password = null;
            if(course.needsPassword()){
                password = gson.fromJson(passwordJson, JsonObject.class);
                if (password == null && password.entrySet().size() == 0) {
                    return Response.status(400).entity("El curso requiere un password").build();
                }
            }
            else{
                password = new JsonObject();
                password.addProperty("password", "");
            }
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
            message.setUser(sender);
            message.setUser1(receiver);
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
            String json = mapper.writeValueAsString(conversation);
            return Response.ok(json).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.status(400).entity(jpe.getMessage()).build();
        }
    }

    @GET
    @Path("{userId}/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions(@PathParam("userId") int userId){
        try{
            List<Question> questions = questionDAO.getAllQuestions();
            String json = mapper.writeValueAsString(questions);
            return Response.ok(json).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe){
            logger.debug(jpe.getMessage(), jpe);
            return Response.status(400).entity(jpe.getMessage()).build();
        }
    }

    @PUT
    @Path("{userId}/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLearningType(@PathParam("userId") int userId, String json){
        try{
            User user = userDAO.get(userId);
            JsonObject results = gson.fromJson(json, JsonObject.class);
            user.setLearningType(FuzzyLearning.inferirAprendizaje(FuzzyLearning.fuzzyVisual(results.get("visual").getAsDouble()),
                    FuzzyLearning.fuzzyAuditivo(results.get("auditivo").getAsDouble()),
                    FuzzyLearning.fuzzyKinestesico(results.get("kinestesico").getAsDouble())));
            userDAO.update(user);
            return Response.ok("Success").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }
}
