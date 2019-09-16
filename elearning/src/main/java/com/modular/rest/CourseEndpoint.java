package com.modular.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.model.Course;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Path("/course")
public class CourseEndpoint {
    private static final Logger logger = Logger.getLogger(CourseEndpoint.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private CourseDAO courseDAO = new CourseDAOImpl();

    static{
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(String json){
        try{
            Course course = mapper.readValue(json, Course.class);
            if(course.getStart().compareTo(course.getEnd()) >= 0){
                return Response.status(400).entity("La fecha de inicio debe ser anterior a la final").build();
            }
            courseDAO.create(course);
            return Response.ok(course.getIdCourse()).build();
        }
        catch(JsonParseException|JsonMappingException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity(json).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch (IOException io) {
            logger.debug(io.getMessage(), io);
            return Response.serverError().entity(io.getMessage()).build();
        }
    }

    @GET
    @Path("{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourse(@PathParam("courseId") int courseId){
        try{
            Course course = courseDAO.get(courseId);
            String courseJson = mapper.writeValueAsString(course);
            return Response.ok(courseJson).build();
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

    @PUT
    @Path("{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId")int courseId, String json){
        try{
            Course course = mapper.readValue(json, Course.class);
            course.setIdCourse(courseId);
            courseDAO.update(course);
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
    @Path("{courseId}")
    public Response deleteCourse(@PathParam("courseId") int courseId){
        try{
            Course course = courseDAO.get(courseId);
            courseDAO.delete(course);
            return Response.ok("Success").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    public Response getAllCourses(){
        try{
            List<Course> courses = courseDAO.getAllCourses();
            for(Course course : courses){
                course.setUsers(null);
                course.setHomework(null);
            }
            String usersJson = mapper.writeValueAsString(courses);
            return Response.ok(usersJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.serverError().entity(jpe.getMessage()).build();
        }
    }

    @GET
    @Path("user/{userId}")
    public Response getAllFromUser(@PathParam("userId") int userId){
        try{
            List<Course> courses = courseDAO.getAllFromUser(userId);
            String coursesJson = mapper.writeValueAsString(courses);
            return Response.ok(coursesJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }
        catch(JsonProcessingException jpe) {
            logger.debug(jpe.getMessage(), jpe);
            return Response.serverError().entity(jpe.getMessage()).build();
        }
    }
}
