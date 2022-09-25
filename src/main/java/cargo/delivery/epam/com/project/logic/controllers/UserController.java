package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import cargo.delivery.epam.com.project.logic.entity.dto.UserDto;
import cargo.delivery.epam.com.project.logic.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RequestParameterMapper requestParameterMapper;
    private final Map<UserRole, String> mapView;

    public ModelAndView login(HttpServletRequest request){
        UserDto userDto = requestParameterMapper.handleRequest(request,UserDto.class);
        User user = userService.getUserByLogin(userDto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(mapView.get(user.getUserRole()));
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        modelAndView.setRedirect(true);
        return modelAndView;
    }
}
