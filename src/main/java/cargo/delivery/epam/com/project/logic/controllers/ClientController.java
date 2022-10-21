package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.PaginationLinksBuilder;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import cargo.delivery.epam.com.project.logic.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final RequestParameterMapper requestParameterMapper;
    private final PaginationLinksBuilder paginationLinksBuilder;
    private final static Long DAYS_FOR_DELIVERY = 3L;


    // /app/cargo/client/create
    public ModelAndView createNewClient(HttpServletRequest request) {
        ClientCreateDto dto = requestParameterMapper.handleRequest(request, ClientCreateDto.class);
        ModelAndView modelAndView = ModelAndView.withView("/client/successRegistration.jsp");
        clientService.createNewClient(dto);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    // app/cargo/client/getWalletInfo
    public ModelAndView getWalletInfo(HttpServletRequest request) {
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        Client client = clientService.getClientById(clientId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/client/clientWallet.jsp");
        modelAndView.addAttribute("client", client);
        return modelAndView;
    }


    // app/cargo/client/topUpWallet
    public ModelAndView topUpClientWallet(HttpServletRequest request) {
        Double amount = Double.parseDouble(request.getParameter("amount"));
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        clientService.topUpClientWallet(amount, clientId);
        ModelAndView modelAndView = ModelAndView.withView("/cargo/client/getWalletInfo?clientId=" + clientId);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    // /app/cargo/client/getClientOrders
    public ModelAndView getClientOrders(HttpServletRequest request) {
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        int page = Integer.parseInt(request.getParameter("page"));
        List<Report> clientOrders = clientService.getOrdersByClientId(clientId, page);
        int countPages = clientService.getCountPagesAllOrdersClient(clientId);
        List<String> paginationLinks = paginationLinksBuilder.buildLinks(request, countPages);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/client/clientOrders.jsp");
        modelAndView.addAttribute("reports", clientOrders);
        modelAndView.addAttribute("paginationLinks", paginationLinks);
        return modelAndView;
    }

    // /app/cargo/client/getInvoice
    public ModelAndView getOrderForInvoice(HttpServletRequest request) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Order order = clientService.getOrderForInvoice(orderId);
        LocalDate date = LocalDate.now();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/client/clientInvoice.jsp");
        modelAndView.addAttribute("order", order);
        modelAndView.addAttribute("date", date);

        return modelAndView;
    }

    // /app/cargo/client/payInvoice
    public ModelAndView payInvoice(HttpServletRequest request) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        int page = Integer.parseInt(request.getParameter("page"));
        LocalDate arrivalDate = Date.valueOf(request.getParameter("arrivalDate")).toLocalDate();
        LocalDate departureDate = arrivalDate.minusDays(DAYS_FOR_DELIVERY);
        clientService.payInvoice(orderId, clientId, departureDate, arrivalDate);
        ModelAndView modelAndView = ModelAndView.withView("/cargo/client/getClientOrders?clientId=" + clientId + "&page=" + page);
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    // /app/cargo/client/getAllOrders/filter
    public ModelAndView filterOrders(HttpServletRequest request) {
        FilteringDto dto = requestParameterMapper.handleRequest(request, FilteringDto.class);
        List<Report> reportList = clientService.filterOrders(dto);
        int countPages = clientService.getCountPagesFiltered(dto);
        List<String> paginationLinks = paginationLinksBuilder.buildLinks(request, countPages);
        ModelAndView modelAndView = ModelAndView.withView("/client/clientOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
        modelAndView.addAttribute("paginationLinks", paginationLinks);
        return modelAndView;
    }

}
