package com.modular.rest;

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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

@Path("/notification")
public class NotificationEndpoint {
    private static final Logger logger = Logger.getLogger(NotificationEndpoint.class);
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
    private NotificationDAO notificationDAO = new NotificationDAOImpl();
    private CourseDAO courseDAO = new CourseDAOImpl();

    @POST
    @Path("{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNotification(@PathParam("courseId") int courseId, String json) {
        try {
            //TODO:Revisar formato de fechas al momento de devolverlas
            if (!courseDAO.exists(courseId)) {
                return Response.status(400).entity("El curso no existe").build();
            }
            Notification notification = gson.fromJson(json, Notification.class);
            notification.setDate(Calendar.getInstance().getTime());
            notificationDAO.create(notification);
            return Response.ok("Success").build();
        } catch (JsonSyntaxException jse) {
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
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
                                       String json) {
        try {
            Notification notification = gson.fromJson(json, Notification.class);
            notification.setIdNotification(notificationId);
            notificationDAO.update(notification);
            return Response.ok("Success").build();
        } catch (JsonSyntaxException jse) {
            logger.debug("The input json was malformed", jse);
            return Response.status(400).entity("The input json was malformed").build();
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
            for (Notification notification : notifications) {
                notification.getCourse().setUsers(null);
            }
            String notificationJson = gson.toJson(notifications);
            return Response.ok(notificationJson).build();
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
