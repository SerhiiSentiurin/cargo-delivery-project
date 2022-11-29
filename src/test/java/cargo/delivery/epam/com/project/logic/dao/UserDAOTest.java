package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Manager;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDAOTest {
    @Mock
    DataSource dataSource;
    @Mock
    Connection connection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;

    @InjectMocks
    UserDAO userDAO;

    private static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    private static final Long ID = 1L;
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(GET_USER_BY_LOGIN)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong("id")).thenReturn(ID);
        when(resultSet.getString("password")).thenReturn(PASSWORD);
    }

    @Test
    public void getUserByLoginWhenReturnClient() throws SQLException {
        User expectedClient = new Client();
        expectedClient.setId(ID);
        expectedClient.setLogin(LOGIN);
        expectedClient.setPassword(PASSWORD);
        expectedClient.setUserRole(UserRole.CLIENT);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("CLIENT");

        Optional<User> resultUser = userDAO.getUserByLogin(LOGIN);
        assertNotNull(resultUser);
        assertTrue(resultUser.isPresent());
        assertEquals(expectedClient, resultUser.get());

        verify(preparedStatement).setString(1, LOGIN);
    }

    @Test
    public void getUserByLoginWhenReturnManager() throws SQLException {
        User expectedManager = new Manager();
        expectedManager.setId(ID);
        expectedManager.setLogin(LOGIN);
        expectedManager.setPassword(PASSWORD);
        expectedManager.setUserRole(UserRole.MANAGER);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("role")).thenReturn("MANAGER");

        Optional<User> resultUser = userDAO.getUserByLogin(LOGIN);
        assertNotNull(resultUser);
        assertTrue(resultUser.isPresent());
        assertEquals(expectedManager, resultUser.get());

        verify(preparedStatement).setString(1, LOGIN);
    }

    @Test
    public void getGetUserByLoginWhenUserNotFound() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        Optional<User> resultUser = userDAO.getUserByLogin(LOGIN);
        assertEquals(Optional.empty(), resultUser);
        assertFalse(resultUser.isPresent());
        assertNotNull(resultUser);

        verify(preparedStatement).setString(1, LOGIN);
    }

}
