package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.UserDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Manager;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UserDAO userDAO;

    @InjectMocks
    UserService userService;

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final UserDto dto = new UserDto(LOGIN,PASSWORD);

    @Test
    public void getUserByLoginWhenUserIsReaderTest(){
        User expectedClient = new Client();
        expectedClient.setLogin(LOGIN);
        expectedClient.setPassword(PASSWORD);

        when(userDAO.getUserByLogin(LOGIN)).thenReturn(Optional.of(expectedClient));

        User resultClient = userService.getUserByLogin(dto);
        assertEquals(expectedClient,resultClient);
    }

    @Test
    public void getUserByLoginWhenUserIsAdminTest(){
        User expectedManager = new Manager();
        expectedManager.setLogin(LOGIN);
        expectedManager.setPassword(PASSWORD);

        when(userDAO.getUserByLogin(LOGIN)).thenReturn(Optional.of(expectedManager));

        User resultManager = userService.getUserByLogin(dto);
        assertEquals(expectedManager,resultManager);
    }

    @Test(expected = AppException.class)
    public void getUserByLoginWhenUserNotFound(){
        when(userDAO.getUserByLogin(LOGIN)).thenReturn(Optional.empty());
        userService.getUserByLogin(dto);
    }

    @Test(expected = AppException.class)
    public void getUserByLoginWhenPasswordIncorrect(){
        User user = new Client();
        user.setLogin(LOGIN);
        user.setPassword("asdf");

        when(userDAO.getUserByLogin(LOGIN)).thenReturn(Optional.of(user));
        userService.getUserByLogin(dto);
    }

}
