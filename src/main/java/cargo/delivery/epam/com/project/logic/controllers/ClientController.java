package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final RequestParameterMapper requestParameterMapper;

    public ModelAndView createNewClient(HttpServletRequest request) {
        ClientCreateDto dto = requestParameterMapper.handleRequest(request, ClientCreateDto.class);
        ModelAndView modelAndView = ModelAndView.withView("/client/successRegistration.jsp");
        clientService.createNewClient(dto);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

}
