package com.robopoker.resources;

import com.jayway.restassured.response.Response;
import com.robopoker.restModel.LoginRequest;
import com.robopoker.restModel.LoginResponse;
import com.robopoker.restModel.RegisterRequest;
import com.robopoker.restModel.RegisterResponse;
import org.junit.Ignore;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class UserResourceITest {

    @Test
    @Ignore //TODO Deal with it
    public void registerNewUser() throws Exception {

        RegisterRequest message = new RegisterRequest();
        message.setEmail("user.mail.com");
        message.setName("user");
        message.setPassword("pass");
        String glassFishPass = "/roboPoker-1.0-SNAPSHOT/rest/register";
        Response response = given()
                .contentType("application/json")
                .body(message)
                .when()
                .post(glassFishPass);
        assertEquals("{\"status\":\"Susses\"}", response.asString());
        RegisterResponse sayHelloResponse = response.as(RegisterResponse.class);
        assertEquals(RegisterResponse.getSussesResponse().getStatus(), sayHelloResponse.getStatus());

    }


    public void setupLoginedUser() throws Exception {

        RegisterRequest message = new RegisterRequest();
        message.setEmail("user1.mail.com");
        message.setName("user1");
        message.setPassword("pass");
        String glassFishPass = "/roboPoker-1.0-SNAPSHOT/rest/register";
        Response response = given()
                .contentType("application/json")
                .body(message)
                .when()
                .post(glassFishPass);
        assertEquals("{\"status\":\"Susses\"}", response.asString());
        RegisterResponse sayHelloResponse = response.as(RegisterResponse.class);
        assertEquals(RegisterResponse.getSussesResponse().getStatus(), sayHelloResponse.getStatus());

    }

    @Test
    @Ignore //TODO Deal with it.
    public void loginUser() throws Exception {
        setupLoginedUser();
        LoginRequest message = new LoginRequest();
        message.setEmail("user1.mail.com");
        message.setPassword("pass");
        String glassFishPass = "/roboPoker-1.0-SNAPSHOT/rest/login";
        Response response = given()
                .contentType("application/json")
                .body(message)
                .when()
                .post(glassFishPass);
        assertEquals("{\"token\":\"Dummy hash\"}", response.asString());
        LoginResponse sayHelloResponse = response.as(LoginResponse.class);
        assertEquals("Dummy hash", sayHelloResponse.getToken());
    }
}
