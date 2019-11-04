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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.log4j.Logger;

import javax.inject.Inject;
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
    static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-mm-dd"));
    }
    @Inject
    private HomeworkDAO homeworkDAO;
    @Inject
    private HomeworkResponseDAO homeworkResponseDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private CourseDAO courseDAO;


    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
    public Response createHomework(Homework homework){
        try{
            //Homework homework = mapper.readValue(json, Homework.class);
            if(!courseDAO.exists(homework.getCourse().getIdCourse())){
                return Response.status(404).entity("The course doesn't exist").build();
            }
            homeworkDAO.create(homework);
            return Response.ok("Success").build();
        } catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createHomework(
            @Multipart(value = "file", required = false) Attachment att1,
            @Multipart(value = "json") Attachment att2
    ) throws IOException {
        Homework hw = mapper.readValue(att2.getDataHandler().getDataSource().getInputStream(), Homework.class);
        if(att1 != null) {
            byte[] file = IOUtils.toByteArray(att1.getDataHandler().getDataSource().getInputStream());
            hw.setResource(file);
        }
        return createHomework(hw);
    }

    @GET
    @Path("{homeworkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomework(@PathParam("homeworkId") int homeworkId){
        try{
            Homework homework = homeworkDAO.get(homeworkId);
            return Response.ok(homework).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    public Response getAllHomeworks(){
        try{
            List<Homework> homework = homeworkDAO.getAll();
            return Response.ok(homework).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{homeworkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateHomework(@PathParam("homeworkId") int homeworkId, Homework homework){
        try{
            homework.setIdHomework(homeworkId);
            homeworkDAO.update(homework);
            return Response.ok("Success").build();
        } catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{homeworkId}/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateHomework(
            @Multipart(value = "file", required = false) Attachment att1,
            @Multipart(value = "json") Attachment att2,
            @PathParam("homeworkId") int homeworkId
    ) throws IOException {
        Homework hw = mapper.readValue(att2.getDataHandler().getDataSource().getInputStream(), Homework.class);
        if(att1 != null) {
            byte[] file = IOUtils.toByteArray(att1.getDataHandler().getDataSource().getInputStream());
            hw.setResource(file);
        }
        return updateHomework(homeworkId, hw);
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
            @Multipart("json") Attachment jsonAttachment
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
                    .header("Content-Disposition", "attachment; filename=\" response." + homeworkResponse.getFileExtension() + "\"")
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
            String textResponse = IOUtils.toString(jsonAttachment.getDataHandler().getInputStream());
            HomeworkResponse response = mapper.readValue(textResponse, HomeworkResponse.class);

            if(uploadedInputStream != null) {
                InputStream stream = uploadedInputStream.getDataHandler().getInputStream();
                byte[] buff = IOUtils.toByteArray(stream);
                response.setResponse(buff);
            }
            homeworkResponseDAO.update(response);
            return Response.ok("Success").build();
        }
        catch(Exception dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("response/grade")
    public Response setGrades(List<HomeworkResponse> responses) {
        try {
            homeworkResponseDAO.setGRades(responses);
            return Response.ok(200).build();
        } catch (DataBaseException e) {
            logger.debug(e.getMessage(),e);
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
