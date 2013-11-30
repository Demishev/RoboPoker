package com.robopoker.services;

import com.robopoker.dbModel.User;
import com.robopoker.restModel.LoginRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Demishev
 * Date: 25.11.13
 * Time: 17:26
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserServices {
    @PersistenceContext
    EntityManager em;

    @POST
    @Path("/login")
    public String loginUser(LoginRequest request) {
        User user = em.find(User.class, request.getLogin());

        if (user.getPasswordHash() == request.getPassword().hashCode()) {

        }

        return null;
    }
}
