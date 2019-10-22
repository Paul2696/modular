package com.modular.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.modular.persistence.dao.CourseDAO;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.NotificationDAO;
import com.modular.persistence.dao.impl.CourseDAOImpl;
import com.modular.persistence.dao.impl.NotificationDAOImpl;
import com.modular.persistence.model.Course;
import com.modular.persistence.model.Notification;
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
import java.util.Set;

@Path("/notification")
public class NotificationEndpoint {
    private static final Logger logger = Logger.getLogger(NotificationEndpoint.class);
    private NotificationDAO notificationDAO = new NotificationDAOImpl();
    @Inject
    private CourseDAO courseDAO;

    @POST
    @Path("{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotification(@PathParam("courseId") int courseId, Notification notification) {
        try {
            //TODO:Revisar formato de fechas al momento de devolverlas
            if (!courseDAO.exists(courseId)) {
                return Response.status(400).entity("El curso no existe").build();
            }
            notification.setDate(Calendar.getInstance().getTime());
            notificationDAO.create(notification);
            return Response.ok("Success").build();
        } catch (DataBaseException dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @POST
    @Path("{courseId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createNotificationFile(
            @PathParam("courseId") int courseId,
            @Multipart("file") Attachment uploadedInputStream,
            @QueryParam("extension") String extension) {
        try {
            if (!courseDAO.exists(courseId)) {
                return Response.status(400).entity("La curso no existe").build();
            }
            InputStream stream = uploadedInputStream.getDataHandler().getInputStream();
            byte[] buff = IOUtils.toByteArray(stream);
            Notification notification = new Notification();
            notification.setResource(buff);
            notification.setMessage("");
            notification.setDate(Calendar.getInstance().getTime());
            notification.setFileExtension(extension);
            notification.setCourse(courseDAO.get(courseId));
            notificationDAO.create(notification);
            return Response.ok("Success").build();
        } catch (Exception dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{courseId}/{notificationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNotification(@PathParam("notificationId") int notificationId,
                                       @PathParam("courseId") int courseId,
                                       Notification notification) {
        try {
            notification.setIdNotification(notificationId);
            notificationDAO.update(notification);
            return Response.ok("Success").build();
        } catch (DataBaseException dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @PUT
    @Path("{courseId}/{notificationId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateNotificationFile(
            @PathParam("courseId") int courseId,
            @PathParam("notificationId") int notificationId,
            @Multipart("file") Attachment uploadedInputStream,
            @QueryParam("extension") String extension) {
        try {
            if (!courseDAO.exists(courseId)) {
                return Response.status(400).entity("La curso no existe").build();
            }
            if (!notificationDAO.exists(notificationId)) {
                return Response.status(400).entity("No existe la notificacion").build();
            }
            InputStream stream = uploadedInputStream.getDataHandler().getInputStream();
            byte[] buff = IOUtils.toByteArray(stream);
            Notification notification = notificationDAO.get(notificationId);
            notification.setResource(buff);
            notification.setMessage("");
            notification.setDate(Calendar.getInstance().getTime());
            notification.setFileExtension(extension);
            notificationDAO.update(notification);
            return Response.ok("Success").build();
        } catch (Exception dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(400).entity(dbe.getMessage()).build();
        }
    }

    @DELETE
    @Path("{notificationId}")
    public Response deleteNotification(@PathParam("notificationId") int notificationId) {
        try {
            if (!notificationDAO.exists(notificationId)) {
                Response.status(400).entity("No existe la notificacion").build();
            }
            Notification notification = notificationDAO.get(notificationId);
            notificationDAO.delete(notification);
            return Response.ok("Success").build();
        } catch (DataBaseException dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNotification(@PathParam("courseId") int courseId) {
        try {
            List<Notification> notifications = notificationDAO.getAllNotifications(courseId);
            return Response.ok(notifications).build();
        } catch (DataBaseException dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        }
    }

    @GET
    @Path("{courseId}/{notificationId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getNotificationFile(@PathParam("courseId") int courseId,
                                        @PathParam("notificationId") int notificationId) {
        try {
            Notification notification = notificationDAO.get(notificationId);
            return Response.ok(notification.getResource(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\" burrada." + notification.getFileExtension() + "\"")
                    .build();
        } catch (DataBaseException dbe) {
            logger.debug(dbe.getMessage(), dbe);
            return Response.status(404).entity(dbe.getMessage()).build();
        }
    }
}
