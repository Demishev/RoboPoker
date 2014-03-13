package aradoblitz.jboss.rest;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.response.Response;
import com.robopoker.restModel.LoginRequest;
import com.robopoker.restModel.LoginResponse;
import com.robopoker.restModel.RegisterRequest;
import com.robopoker.restModel.RegisterResponse;


public class SayHelloRestServiceIT {

	
	@Test
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
	
	@Test
	public void sayHelloInItalian() throws Exception {
		LoginRequest message = new LoginRequest();
		message.setEmail("user.mail.com");
		message.setPassword("pass");
		String glassFishPass = "/roboPoker-1.0-SNAPSHOT/rest/login";
		Response response = given()
				.contentType("application/json")
				.body(message)
			.when()
				.post(glassFishPass);
		assertEquals("{\"token\":\"Some mock token\"}", response.asString());
		LoginResponse sayHelloResponse = response.as(LoginResponse.class);
		assertEquals("Some mock token", sayHelloResponse.getToken());
	}
}
