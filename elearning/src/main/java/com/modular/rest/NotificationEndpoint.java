package com.modular.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.NotificationDAO;
import com.modular.persistence.dao.impl.NotificationDAOImpl;
import com.modular.persistence.model.Notification;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notification")
public class NotificationEndpoint {
    private static final Logger logger = Logger.getLogger(NotificationEndpoint.class);
    private Gson gson = new Gson();
    private NotificationDAO notificationDAO = new NotificationDAOImpl();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotification(String json){
        try{
            Notification notification = gson.fromJson(json, Notification.class);
            notificationDAO.create(notification);
            return Response.ok().build();
        }
        catch(JsonSyntaxException jse){
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(),dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }
    /*
    @GET
    @Path("{notificationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotification*/

    @PUT
    @Path("{notificationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNotification(@PathParam("notificationId") int notificationId, String json){
        try{
            Notification notification = gson.fromJson(json, Notification.class);
            notificationDAO.update(notification);
            return Response.ok().build();
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
    @Path("{notificationId}")
    public Response deleteNotification(@PathParam("notificationId") int notificationId){
        try{
            Notification notification = notificationDAO.get(notificationId);
            notificationDAO.delete(notification);
            return Response.ok().build();
        }
        catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }
}
