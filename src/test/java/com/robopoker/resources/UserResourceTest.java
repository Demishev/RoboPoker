package com.robopoker.resources;

import com.robopoker.dbModel.User;
import com.robopoker.restModel.RegisterRequest;
import com.robopoker.restModel.RegisterResponse;
import com.robopoker.utilities.DateGenerator;
import com.robopoker.utilities.PasswordHashGenerator;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * User: Demishev
 * Date: 25.12.13
 * Time: 11:41
 */
public class UserResourceTest {
    public static final String SOME_EMAIL = "Some email";
    public static final String SOME_NAME = "Some name";
    public static final String SOME_PASSWORD = "Some password";
    public static final String SOME_PASSWORD_HASH = "Some password hash";

    private final RegisterRequest registerRequest = new RegisterRequest(SOME_EMAIL, SOME_NAME, SOME_PASSWORD);
    private UserResource userResource;

    private EntityManager entityManagerMock;
    private Logger loggerMock;
    private PasswordHashGenerator passwordHashGeneratorMock;
    private DateGenerator dateGeneratorMock;
    private Date dateMock;

    @Before
    public void setUp() throws Exception {
        userResource = new UserResource();

        entityManagerMock = mock(EntityManager.class);
        loggerMock = mock(Logger.class);
        passwordHashGeneratorMock = mock(PasswordHashGenerator.class);
        dateGeneratorMock = mock(DateGenerator.class);

        userResource.setEm(entityManagerMock);
        userResource.setLogger(loggerMock);
        userResource.setPasswordHashGenerator(passwordHashGeneratorMock);
        userResource.setDateGenerator(dateGeneratorMock);

        dateMock = mock(Date.class);
        when(dateGeneratorMock.getCurrentDate()).thenReturn(dateMock);

        when(passwordHashGeneratorMock.generateHashFromPassword(SOME_PASSWORD)).thenReturn(SOME_PASSWORD_HASH);
    }

    @Test
    public void shouldStatusSussesWhenRegisterNewUser() throws Exception {
        assertEquals(RegisterResponse.getSussesResponse(), userResource.registerUser(registerRequest));
    }

    @Test
    public void shouldStatusDuplicatesWhenRegisterNewUserContainUserWithEmail() throws Exception {
        when(entityManagerMock.find(User.class, SOME_EMAIL)).thenReturn(new User());

        assertEquals(RegisterResponse.getUserDuplicates(), userResource.registerUser(registerRequest));
    }

    @Test
    public void shouldRegisteredUserPersistIntoDBWhenUserRegistered() throws Exception {
        userResource.registerUser(registerRequest);

        User newUser = new User(SOME_EMAIL, SOME_NAME, SOME_PASSWORD_HASH, dateMock);

        verify(entityManagerMock).persist(newUser);
    }
}
