package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import cargo.delivery.epam.com.project.logic.entity.dto.UserDto;
import cargo.delivery.epam.com.project.logic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    UserService userService;
    @Mock
    RequestParameterMapper requestParameterMapper;
    @Mock
    Map<UserRole,String> mapView;
    @Mock
    ModelAndView modelAndView;
    @Mock
    HttpSession session;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    UserController userController;

    private static final UserDto userDto = Mockito.mock(UserDto.class);


}
