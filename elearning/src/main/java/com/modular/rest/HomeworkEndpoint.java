package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


@Path("/homework")
public class HomeworkEndpoint {
    private static final Logger logger = Logger.getLogger(HomeworkEndpoint.class);
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
    private HomeworkDAO homeworkDAO = new HomeworkDAOImpl();
    private HomeworkResponseDAO homeworkResponseDAO = new HomeworkResponseDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();
    private CourseDAO courseDAO = new CourseDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createHomework(String json){
        try{
            Homework homework = gson.fromJson(json, Homework.class);
            if(courseDAO.exists(homework.getIdCourse())){
                return Response.status(404).entity("The course doesn't exist").build();
            }
            homeworkDAO.create(homework);
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
    @Path("{homeworkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomework(@PathParam("homeworkId") int homeworkId){
        try{
            Homework homework = homeworkDAO.get(homeworkId);
            String homeworkJson = gson.toJson(homework);
            return Response.ok(homeworkJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    public Response getAllHomeworks(){
        try{
            Set<Homework> homeworks = homeworkDAO.getAll();
            String homeworksJson = gson.toJson(homeworks);
            return Response.ok(homeworksJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{homeworkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateHomework(@PathParam("homeworkId") int homeworkId, String json){
        try{
            Homework homework = gson.fromJson(json, Homework.class);
            homework.setIdHomework(homeworkId);
            homeworkDAO.update(homework);
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
            Course course = courseDAO.get(homeworkDAO.get(homeworkId).getIdCourse());
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
            HomeworkResponse response = gson.fromJson(textResponse, HomeworkResponse.class);
            response.setResponse(buff);
            response.setIdHomework(homeworkDAO.get(homeworkId));
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
            homeworkResponse.getIdHomework().setHomeworkResponse(null);
            String homeworkJson = gson.toJson(homeworkResponse);
            return Response.ok(homeworkJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
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
            Course course = courseDAO.get(homeworkDAO.get(homeworkId).getIdCourse());
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
            HomeworkResponse response = gson.fromJson(textResponse, HomeworkResponse.class);
            response.setResponse(buff);
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
