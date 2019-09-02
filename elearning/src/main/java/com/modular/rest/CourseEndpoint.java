package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Set;

@Path("/course")
public class CourseEndpoint {
    private static final Logger logger = Logger.getLogger(CourseEndpoint.class);
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
    private CourseDAO courseDAO = new CourseDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(String json){
        try{
            Course course = gson.fromJson(json, Course.class);
            if(course.getStart().compareTo(course.getEnd()) >= 0){
                return Response.status(400).entity("La fecha de inicio debe ser anterior a la final").build();
            }
            courseDAO.create(course);
            return Response.ok(course.getIdCourse()).build();
        }
        catch(JsonSyntaxException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity(json).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourse(@PathParam("courseId") int courseId){
        try{
            Course course = courseDAO.get(courseId);
            for(User user : course.getUsers()){
                user.setCourses(null);
            }
            String courseJson = gson.toJson(course);
            return Response.ok(courseJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId")int courseId, String json){
        try{
            Course course = gson.fromJson(json, Course.class);
            course.setIdCourse(courseId);
            courseDAO.update(course);
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
            Set<Course> courses = courseDAO.getAllCourses();
            for(Course course : courses){
                for(User user : course.getUsers()){
                    user.setCourses(null);
                }
            }
            String usersJson = gson.toJson(courses);
            return Response.ok(usersJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{userId}")
    public Response getAllFromUser(@PathParam("userId") int userId){
        try{
            Set<Course> courses = courseDAO.getAllFromUser(userId);
            for (Course course : courses){
                for (User user : course.getUsers()){
                    user.setCourses(null);
                }
            }
            String coursesJson = gson.toJson(courses);
            return Response.ok(coursesJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }
    }
}
