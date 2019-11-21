package com.modular.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.modular.persistence.dao.DataBaseException;
import com.modular.persistence.dao.UserDAO;
import com.modular.persistence.model.User;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class SignUp {
    private static final Logger logger = Logger.getLogger(UserEndpoint.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private Gson gson = new Gson();

    @Inject
    private UserDAO userDAO ;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user){
        try{
            logger.debug("Request json: " + user);
            //User user = mapper.readValue(json, User.class);
            if(user.getUserType().getIdUserType() == 1){
                user.getUserType().setName("teacher");
            }
            if(user.getUserType().getIdUserType() == 2){
                user.getUserType().setName("student");
            }
            userDAO.create(user);
            return Response.ok(200).build();
        } catch(DataBaseException dbe){
            logger.debug(dbe.getMessage(), dbe);
            return Response.serverError().entity(dbe.getMessage()).build();
        } catch (Exception io) {
            logger.debug(io.getMessage(), io);
            return Response.serverError().entity(io.getMessage()).build();
        }
    }
}
