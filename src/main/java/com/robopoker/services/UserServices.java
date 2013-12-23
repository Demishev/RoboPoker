package com.robopoker.services;

import com.robopoker.dbModel.User;
import com.robopoker.restModel.LoginRequest;
import com.robopoker.restModel.LoginResponse;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
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
@Stateful
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserServices {
    @Inject
    EntityManager em;

    @POST
    @Path("/login")
    public LoginResponse loginUser(LoginRequest request) {
    //	em = Persistence.createEntityManagerFactory("userServices").createEntityManager();
        User user = em.find(User.class, request.getLogin());

        if (user.getPasswordHash() == request.getPassword().hashCode()) {

        }

    	LoginResponse loginResponse = new LoginResponse();
    	
    	loginResponse.setToken("Some mock token");
    	
        return loginResponse;
    }
}
