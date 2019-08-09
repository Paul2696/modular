package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.HomeworkDAO;
import com.modular.persistence.dao.impl.HomeworkDAOImpl;
import com.modular.persistence.model.Homework;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/homework")
public class HomeworkEndpoint {
    private static final Logger logger = Logger.getLogger(HomeworkEndpoint.class);
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
    private HomeworkDAO homeworkDAO = new HomeworkDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createHomework(String json){
        try{
            Homework homework = gson.fromJson(json, Homework.class);
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

}
