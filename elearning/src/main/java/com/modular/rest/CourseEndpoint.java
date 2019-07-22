package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.model.Course;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;

@Path("/course")
public class CourseEndpoint {
    private static final Logger logger = Logger.getLogger(CourseEndpoint.class);
    private Gson gson = new Gson();
    private CourseDAO courseDAO = new CourseDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(String json){
        Course course = new Course();
        try{
            Gson gson = new GsonBuilder().setDateFormat("EEE, MM-dd-yyyy HH:mm:ss'Z'").create();
            course = gson.fromJson(json, Course.class);
            System.out.println(course.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(course.getStart());
            if(calendar.after(course.getEnd())){
                return Response.status(400).entity("La fecha de inicio debe ser anterior a la final").build();
            }
            courseDAO.create(course);
            return Response.ok("Success").build();
        }
        catch(JsonSyntaxException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity(course.toString()).build();
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
}
