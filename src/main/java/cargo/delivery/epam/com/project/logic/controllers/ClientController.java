package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final RequestParameterMapper requestParameterMapper;


    // /app/cargo/client/create
    public ModelAndView createNewClient(HttpServletRequest request) {
        ClientCreateDto dto = requestParameterMapper.handleRequest(request, ClientCreateDto.class);
        ModelAndView modelAndView = ModelAndView.withView("/client/successRegistration.jsp");
        clientService.createNewClient(dto);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    // app/cargo/client/getWalletInfo
    public ModelAndView getWalletInfo(HttpServletRequest request){
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        Client client = clientService.getClientById(clientId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/client/clientWallet.jsp");
        modelAndView.addAttribute("client",client);
        return modelAndView;
    }


    // app/cargo/client/topUpWallet
    public ModelAndView topUpClientWallet(HttpServletRequest request){
        Double amount = Double.parseDouble(request.getParameter("amount"));
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        clientService.topUpClientWallet(amount,clientId);
        ModelAndView modelAndView = ModelAndView.withView("/cargo/client/getWalletInfo?clientId="+clientId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

}
