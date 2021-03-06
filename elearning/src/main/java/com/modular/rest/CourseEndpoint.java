package com.modular.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.dao.impl.UserDAOImpl;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.User;
import com.modular.persistence.model.UserType;
import org.apache.log4j.Logger;

import javax.inject.Inject;
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
    @Inject
    private CourseDAO courseDAO;
    @Inject
    private UserDAO userDAO;

    static{
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourse(Course course){
        try{
            logger.debug("Request JSON: " + course);
            //Course course = mapper.readValue(json, Course.class);
            if(course.getStart().compareTo(course.getEnd()) >= 0){
                return Response.status(400).entity("La fecha de inicio debe ser anterior a la final").build();
            }
            courseDAO.create(course);
            return Response.status(200).entity(course.getIdCourse()).build();
        } catch(DataBaseException dbe){
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
    public Response updateCourse(@PathParam("courseId")int courseId, Course course){
        try{
            //Course course = mapper.readValue(json, Course.class);
            if(course.getStart().compareTo(course.getEnd()) >= 0){
                return Response.status(400).entity("La fecha de inicio debe ser anterior a la final").build();
            }
            course.setIdCourse(courseId);
            courseDAO.update(course);
            return Response.ok("Success").build();
        } catch(DataBaseException dbe){
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
            return Response.ok(200).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourses(){
        try{
            List<Course> courses = courseDAO.getAllCourses();
            for(Course course : courses){
                course.setUsers(null);
                course.setHomework(null);
                course.getUser().setCourses(null);
            }
            //String usersJson = mapper.writeValueAsString(courses);
            return Response.ok(courses).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("teacher/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFromTeacher(@PathParam("userId") int userId){
        try{
            User user = userDAO.get(userId);
            if(user.getUserType().getIdUserType() != UserType.TEACHER && user.getUserType().getIdUserType() != UserType.ADMIN){
                return Response.status(403).entity("Se necesita ser un profesor").build();
            }
            List<Course> courses = courseDAO.getAllFromTeacher(userId);
            //String coursesJson = mapper.writeValueAsString(courses);
            return Response.ok(courses).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("student/{userId}")
    public Response getAllFromStudent(@PathParam("userId") int userId){
        try{
            User user = userDAO.get(userId);
            if(user.getUserType().getIdUserType() != UserType.STUDENT){
                return Response.status(403).entity("Se necesita ser un estudiante").build();
            }
            List<Course> courses = courseDAO.getAllFromStudent(userId);
            String coursesJson = mapper.writeValueAsString(courses);
            return Response.ok(coursesJson).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(Exception e){
            logger.debug("Algo salio mal");
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{idCourse}/users")
    public Response getAllUsers(@PathParam("idCourse") int idCourse){
        try {
            if(!courseDAO.exists(idCourse)){
                return Response.status(404).entity("No existe el curso").build();
            }
            List<User> users = courseDAO.getAllUsersFromCourse(idCourse);
            return Response.ok(users).build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
        catch(Exception e){
            logger.debug("Algo salio mal");
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
