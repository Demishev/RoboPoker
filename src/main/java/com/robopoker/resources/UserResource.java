package com.robopoker.resources;

import com.robopoker.dbModel.User;
import com.robopoker.restModel.LoginRequest;
import com.robopoker.restModel.LoginResponse;
import com.robopoker.restModel.RegisterRequest;
import com.robopoker.restModel.RegisterResponse;
import com.robopoker.utilities.DateGenerator;
import com.robopoker.utilities.PasswordHashGenerator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;


/**
 * User: Demishev
 * Date: 25.11.13
 * Time: 17:26
 */
@Path("/rest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource extends Application {
    @Inject
    private Logger logger;

    @Inject
    private EntityManager em;

    @Inject
    private PasswordHashGenerator passwordHashGenerator;

    @Inject
    DateGenerator dateGenerator;

    @POST
    @Path("/register")
    public RegisterResponse registerUser(RegisterRequest request) {
        logger.info("Register request \n" + request.toString());

        User user = em.find(User.class, request.getEmail());

        if (user != null) {
            logger.info("User with this email already exists");
            return RegisterResponse.getUserDuplicates();
        }

        user = generateNewUser(request.getEmail(), request.getName(), request.getPassword());

        logger.info("Persisting new user to DB: \n" + user.toString());
        em.persist(user);

        return RegisterResponse.getSussesResponse();
    }


    @POST
    @Path("/login")
    public LoginResponse loginUser(LoginRequest request) {
        logger.info("Loggining request \n " + request.toString());
        User user = em.find(User.class, request.getEmail());
        
        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(user.getPasswordHash());


        logger.info(loginResponse.toString());
        return loginResponse;
    }

    private User generateNewUser(String email, String name, String password) {
        return new User(email, name, passwordHashGenerator.generateHashFromPassword(password), dateGenerator.getCurrentDate());
    }


    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void setPasswordHashGenerator(PasswordHashGenerator passwordHashGenerator) {
        this.passwordHashGenerator = passwordHashGenerator;
    }

    public void setDateGenerator(DateGenerator dateGenerator) {
        this.dateGenerator = dateGenerator;
    }
}
