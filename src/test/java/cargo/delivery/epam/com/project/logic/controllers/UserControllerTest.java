package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Manager;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import cargo.delivery.epam.com.project.logic.entity.dto.UserDto;
import cargo.delivery.epam.com.project.logic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    UserService userService;
    @Mock
    RequestParameterMapper requestParameterMapper;
    @Mock
    Map<UserRole, String> mapView;
    @Mock
    HttpSession session;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    UserController userController;

    private static final UserDto userDto = Mockito.mock(UserDto.class);
    private static final String LOCALE = "selectedLocale";

    @Test
    public void loginForClientTest() {
        User expectedClient = Mockito.mock(Client.class);
        when(requestParameterMapper.handleRequest(request, UserDto.class)).thenReturn(userDto);
        when(userService.getUserByLogin(userDto)).thenReturn(expectedClient);
        when(mapView.get(expectedClient.getUserRole())).thenReturn("/client/home.jsp");
        when(request.getSession(true)).thenReturn(session);

        ModelAndView modelAndView = userController.login(request);
        assertNotNull(modelAndView);
        assertEquals("/client/home.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getSession(true);
        verify(session).setAttribute("user", expectedClient);
    }

    @Test
    public void loginForManagerTest() {
        User expectedManager = Mockito.mock(Manager.class);
        when(requestParameterMapper.handleRequest(request, UserDto.class)).thenReturn(userDto);
        when(userService.getUserByLogin(userDto)).thenReturn(expectedManager);
        when(mapView.get(expectedManager.getUserRole())).thenReturn("/manager/home.jsp");
        when(request.getSession(true)).thenReturn(session);

        ModelAndView modelAndView = userController.login(request);
        assertNotNull(modelAndView);
        assertEquals("/manager/home.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(request).getSession(true);
        verify(session).setAttribute("user", expectedManager);
    }

    @Test
    public void logoutTest() {
        when(request.getSession(false)).thenReturn(session);
        ModelAndView modelAndView = userController.logout(request);
        assertNotNull(modelAndView);
        assertEquals("/index.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void changeLocaleOnIndexPageTest() {
        when(request.getParameter("selectedLocale")).thenReturn(LOCALE);
        when(request.getHeader("referer")).thenReturn("http://localhost:8080/app/index.jsp");
        when(request.getSession(false)).thenReturn(session);
        Locale expectedLocale = new Locale(LOCALE);

        ModelAndView modelAndView = userController.changeLocale(request);
        assertNotNull(modelAndView);
        assertEquals("/index.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(session).setAttribute("selectedLocale", expectedLocale);
        verify(request).getSession(false);
    }

    @Test
    public void changeLocaleOnUserPageTest() {
        when(request.getParameter("selectedLocale")).thenReturn(LOCALE);
        when(request.getHeader("referer")).thenReturn("http://localhost:8080/app/client/home.jsp");
        when(request.getSession(false)).thenReturn(session);
        Locale expectedLocale = new Locale(LOCALE);

        ModelAndView modelAndView = userController.changeLocale(request);
        assertNotNull(modelAndView);
        assertEquals("/client/home.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(session).setAttribute("selectedLocale", expectedLocale);
        verify(request).getSession(false);
    }
}
