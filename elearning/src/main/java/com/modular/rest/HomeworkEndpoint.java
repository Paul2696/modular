package com.modular.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modular.persistence.dao.*;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.dao.impl.HomeworkDAOImpl;
import com.modular.persistence.dao.impl.HomeworkResponseDAOImpl;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.Homework;
import com.modular.persistence.model.HomeworkResponse;
import com.modular.persistence.model.User;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


@Path("/homework")
public class HomeworkEndpoint {
    private static final Logger logger = Logger.getLogger(HomeworkEndpoint.class);
    private static ObjectMapper mapper = new ObjectMapper();

    private HomeworkDAO homeworkDAO = new HomeworkDAOImpl();
    private HomeworkResponseDAO homeworkResponseDAO = new HomeworkResponseDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private CourseDAO courseDAO = new CourseDAOImpl();
    static{
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public Response createHomework(String json){
        try{
            Homework homework = mapper.readValue(json, Homework.class);
            if(!courseDAO.exists(homework.getCourse().getIdCourse())){
                return Response.status(404).entity("The course doesn't exist").build();
            }
            homeworkDAO.create(homework);
            return Response.ok("Success").build();
        }
        catch(JsonParseException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(JsonMappingException jme) {
            logger.debug("The input json was malformed", jme);
            return Response.status(400).entity(json).build();
        }
        catch (IOException io) {
            logger.debug(io.getMessage(), io);
            return Response.serverError().entity(io.getMessage()).build();
        }
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createHomework(
            @Multipart("file") Attachment file,
            @Multipart("json") Attachment json
    ) {
        return null;
    }

    @GET
    @Path("{homeworkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomework(@PathParam("homeworkId") int homeworkId){
        try{
            Homework homework = homeworkDAO.get(homeworkId);
            String homeworkJson = mapper.writeValueAsString(homework);
            return Response.ok(homeworkJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.status(400).entity(jpe.getMessage()).build();
        }
    }

    @GET
    public Response getAllHomeworks(){
        try{
            List<Homework> homeworks = homeworkDAO.getAll();
            String homeworksJson = mapper.writeValueAsString(homeworks);
            return Response.ok(homeworksJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.status(400).entity(jpe.getMessage()).build();
        }
    }

    @PUT
    @Path("{homeworkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateHomework(@PathParam("homeworkId") int homeworkId, String json){
        try{
            Homework homework = mapper.readValue(json, Homework.class);
            homework.setIdHomework(homeworkId);
            homeworkDAO.update(homework);
            return Response.ok("Success").build();
        }
        catch(JsonParseException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(JsonMappingException jme) {
            logger.debug("The input json was malformed", jme);
            return Response.status(400).entity(json).build();
        }
        catch (IOException io) {
            logger.debug(io.getMessage(), io);
            return Response.serverError().entity(io.getMessage()).build();
        }
    }

    @DELETE
    @Path("{homeworkId}")
    public Response deleteHomework(@PathParam("homeworkId") int homeworkId){
        try{
            Homework homework = homeworkDAO.get(homeworkId);
            homeworkDAO.delete(homework);
            return Response.ok("Success").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @POST
    @Path("{homeworkId}/response/{userId}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createHomeworkResponseFile(
            @PathParam("homeworkId") int homeworkId,
            @PathParam("userId") int userId,
            @Multipart(value = "file", required = false) Attachment uploadedInputStream,
            @Multipart("json") Attachment jsonAttachment,
            @QueryParam("extension") String extension
            )
    {
        try{
            if(!homeworkDAO.exists(homeworkId)){
                return Response.status(400).entity("La tarea con id " + homeworkId + " no existe").build();
            }
            if(!userDAO.exist(userId)){
                return Response.status(400).entity("El usuario " + userId + " no existe").build();
            }
            Course course = courseDAO.get(homeworkDAO.get(homeworkId).getCourse().getIdCourse());
            User user = userDAO.get(userId);
            boolean found = false;
            for(Course c : user.getCourses()){
                if(c.getIdCourse() == course.getIdCourse()){
                    found = true;
                    break;
                }
            }
            if(!found){
                return Response.status(400).entity("El usuario " + userId + " no esta en el curso").build();
            }
            String textResponse = IOUtils.toString(jsonAttachment.getDataHandler().getInputStream());
            HomeworkResponse response = mapper.readValue(textResponse, HomeworkResponse.class);

            if(uploadedInputStream != null) {
                InputStream stream = uploadedInputStream.getDataHandler().getInputStream();
                byte[] buff = IOUtils.toByteArray(stream);
                response.setResponse(buff);
            }

            response.setHomework(homeworkDAO.get(homeworkId));
            response.setIdUser(userId);
            response.setSended(Calendar.getInstance().getTime());
            response.setFileExtension(extension);
            response.setSent(true);
            homeworkResponseDAO.create(response);
            return Response.ok("Success").build();
        }
        catch(Exception dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{homeworkId}/response/{userId}/{homeworkResponseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeworkResponse(
            @PathParam("homeworkId") int homeworkId,
            @PathParam("userId") int userId,
            @PathParam("homeworkResponseId") int homeworkResponseId
            )
    {
        try{
            HomeworkResponse homeworkResponse = homeworkResponseDAO.get(homeworkResponseId);
            homeworkResponse.getHomework().setResponses(null);
            String homeworkJson = mapper.writeValueAsString(homeworkResponse);
            return Response.ok(homeworkJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.status(400).entity(jpe.getMessage()).build();
        }
    }

    @GET
    @Path("{homeworkId}/response/{userId}/{homeworkResponseId}/file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHomeworkResponseFile(
            @PathParam("homeworkId") int homeworkId,
            @PathParam("userId") int userId,
            @PathParam("homeworkResponseId") int homeworkResponseId
            )
    {
        try{
            HomeworkResponse homeworkResponse = homeworkResponseDAO.get(homeworkResponseId);
            return Response.ok(homeworkResponse.getResponse(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\" burrada." + homeworkResponse.getFileExtension() + "\"")
                    .build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }

    }

    @PUT
    @Path("{homeworkId}/response/{userId}/{homeworkResponseId}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateHomeworkFile(
            @PathParam("homeworkId") int homeworkId,
            @PathParam("userId") int userId,
            @PathParam("homeworkResponseId") int homeworkResponseId,
            @Multipart("file") Attachment uploadedInputStream,
            @Multipart("json") Attachment jsonAttachment,
            @QueryParam("extension") String extension
            )
    {
        try{
            if(!homeworkDAO.exists(homeworkId)){
                return Response.status(400).entity("La tarea con id " + homeworkId + " no existe").build();
            }
            if(!userDAO.exist(userId)){
                return Response.status(400).entity("El usuario " + userId + " no existe").build();
            }
            Course course = courseDAO.get(homeworkDAO.get(homeworkId).getCourse().getIdCourse());
            User user = userDAO.get(userId);
            boolean found = false;
            for(Course c : user.getCourses()){
                if(c.getIdCourse() == course.getIdCourse()){
                    found = true;
                    break;
                }
            }
            if(!found){
                return Response.status(400).entity("El usuario " + userId + " no esta en el curso").build();
            }
            InputStream stream = uploadedInputStream.getDataHandler().getInputStream();
            byte[] buff = IOUtils.toByteArray(stream);
            String textResponse = IOUtils.toString(jsonAttachment.getDataHandler().getInputStream());
            HomeworkResponse response = mapper.readValue(textResponse, HomeworkResponse.class);
            response.setResponse(buff);
            response.setHomework(homeworkDAO.get(homeworkId));
            response.setIdHomeworkResponse(homeworkResponseId);
            response.setIdUser(userId);
            response.setSended(Calendar.getInstance().getTime());
            response.setFileExtension(extension);
            homeworkResponseDAO.update(response);
            return Response.ok("Success").build();
        }
        catch(Exception dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    //TODO: Agregar metodo para calificar
}
