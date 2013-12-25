package com.robopoker.resources;

import com.robopoker.PasswordHashGenerator;
import com.robopoker.dbModel.User;
import com.robopoker.restModel.RegisterRequest;
import com.robopoker.restModel.RegisterResponse;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Demishev
 * Date: 25.12.13
 * Time: 11:41
 */
public class UserResourceTest {
    private UserResource userResource;

    private EntityManager entityManagerMock;
    private Logger loggerMock;
    private PasswordHashGenerator passwordHashGeneratorMock;

    @Before
    public void setUp() throws Exception {
        userResource = new UserResource();

        entityManagerMock = mock(EntityManager.class);
        loggerMock = mock(Logger.class);
        passwordHashGeneratorMock = mock(PasswordHashGenerator.class);

        userResource.setEm(entityManagerMock);
        userResource.setLogger(loggerMock);
        userResource.setPasswordHashGenerator(passwordHashGeneratorMock);
    }

    @Test
    public void shouldStatusSussesWhenRegisterNewUser() throws Exception {
        final RegisterRequest request = new RegisterRequest("Some email", "Some name", "Some password");

        assertEquals(RegisterResponse.getSussesResponse(), userResource.registerUser(request));
    }

    @Test
    public void shouldStatusDuplicatesWhenRegisterNewUserContainUserWithEmail() throws Exception {
        final RegisterRequest request = new RegisterRequest("Some email", "Some name", "Some password");

        when(entityManagerMock.find(User.class, "Some email")).thenReturn(new User());

        assertEquals(RegisterResponse.getUserDuplicates(), userResource.registerUser(request));
    }
}
