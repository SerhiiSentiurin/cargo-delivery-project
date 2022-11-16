package cargo.delivery.epam.com.project.infrastructure.config.servlet;

import cargo.delivery.epam.com.project.infrastructure.config.ConfigLoader;
import cargo.delivery.epam.com.project.infrastructure.config.db.ConfigDataSource;
import cargo.delivery.epam.com.project.infrastructure.config.db.ConfigLiquibase;
import cargo.delivery.epam.com.project.infrastructure.web.*;
import cargo.delivery.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import cargo.delivery.epam.com.project.infrastructure.web.filter.encoding.EncodingFilter;
import cargo.delivery.epam.com.project.infrastructure.web.filter.security.SecurityFilter;
import cargo.delivery.epam.com.project.infrastructure.web.listener.SessionListenerLocales;
import cargo.delivery.epam.com.project.infrastructure.web.pagination.PaginationLinksBuilder;
import cargo.delivery.epam.com.project.logic.controllers.ClientController;
import cargo.delivery.epam.com.project.logic.controllers.OrderController;
import cargo.delivery.epam.com.project.logic.controllers.ManagerController;
import cargo.delivery.epam.com.project.logic.controllers.UserController;
import cargo.delivery.epam.com.project.logic.dao.*;
import cargo.delivery.epam.com.project.logic.dao.filtering.SetterFilteredFieldToPreparedStatement;
import cargo.delivery.epam.com.project.logic.dao.filtering.PreparerQueryToFiltering;
import cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering.*;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import cargo.delivery.epam.com.project.logic.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.util.*;

@Log4j2
public class FrontServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {

        servletContext.addListener(createSessionListenerLocales());

        FilterRegistration.Dynamic security = servletContext.addFilter("security", new SecurityFilter());
        security.addMappingForUrlPatterns(null, false, "/*");
        FilterRegistration.Dynamic encoding = servletContext.addFilter("encoding", new EncodingFilter());
        encoding.addMappingForUrlPatterns(null, false, "/*");

        FrontServlet frontServlet = createFrontServlet();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(frontServlet.getServletName(), frontServlet);
        dynamic.setLoadOnStartup(0);
        dynamic.addMapping("/cargo/*");
        log.info("Front Servlet was started");

    }

    private SessionListenerLocales createSessionListenerLocales() {
        List<Locale> locales = new ArrayList<>();
        Locale selectedLocale = new Locale("en");
        locales.add(selectedLocale);
        locales.add(new Locale("ua"));
        return new SessionListenerLocales(locales, selectedLocale);
    }

    private FrontServlet createFrontServlet() {
        ProcessorRequest processorRequest = createProcessorRequest();
        ProcessorModelAndView processorModelAndView = new ProcessorModelAndView();
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        return new FrontServlet("frontServlet", processorRequest, processorModelAndView, exceptionHandler);
    }

    private ProcessorRequest createProcessorRequest() {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.loadConfigurations("app.yaml");
        List<Placeholder> placeholders = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestParameterMapper requestParameterMapper = new RequestParameterMapper(objectMapper);

        DataSource dataSource = new ConfigDataSource().createDataSource(configLoader);
        ConfigLiquibase configLiquibase = new ConfigLiquibase(dataSource);
        configLiquibase.updateDatabase(configLoader);

        UserController userController = createUserController(requestParameterMapper, dataSource);
        ClientController clientController = createClientController(requestParameterMapper, dataSource);
        OrderController orderController = createOrderController(requestParameterMapper, dataSource);
        ManagerController managerController = createManagerController(requestParameterMapper, dataSource);

        placeholders.add(new Placeholder("POST", "login", userController::login));
        placeholders.add(new Placeholder("POST", "logout", userController::logout));
        placeholders.add(new Placeholder("POST", "changeLocale", userController::changeLocale));
        placeholders.add(new Placeholder("POST", "client/create", clientController::createNewClient));
        placeholders.add(new Placeholder("GET", "client/wallet", clientController::getWalletInfo)); //getWalletInfo
        placeholders.add(new Placeholder("POST", "client/wallet", clientController::topUpClientWallet)); // topUpWallet
        placeholders.add(new Placeholder("GET", "client/routes", orderController::getRoutesForRegisterUser));
        placeholders.add(new Placeholder("GET", "client/calculate/delivery", orderController::getDeliveryCostForRegisteredUser)); // calculateDelivery
        placeholders.add(new Placeholder("POST", "client/create/order", orderController::createOrder)); // createOrder
        placeholders.add(new Placeholder("GET", "client/orders", clientController::getClientOrders)); // getClientOrders
        placeholders.add(new Placeholder("GET", "client/invoice", clientController::getOrderForInvoice)); // getInvoice
        placeholders.add(new Placeholder("POST", "client/invoice", clientController::payInvoice)); // payInvoice
        placeholders.add(new Placeholder("GET", "client/orders/filter", clientController::filterOrders)); // getAllOrders/filter
        placeholders.add(new Placeholder("GET", "manager/orders", managerController::getAllOrders)); // getAllOrders
        placeholders.add(new Placeholder("GET", "manager/orders/notConfirmed", managerController::getNotConfirmedOrders)); // getNotConfirmedOrders
        placeholders.add(new Placeholder("POST", "manager/orders/confirm", managerController::confirmOrder)); // confirmOrder
        placeholders.add(new Placeholder("GET", "manager/orders/filter", managerController::filterReports)); // getAllOrders/filter
        placeholders.add(new Placeholder("GET", "manager/report", managerController::getReportByDayAndDirection)); // getReport
        placeholders.add(new Placeholder("GET", "routes", orderController::getRoutesForNonRegisterUser));
        placeholders.add(new Placeholder("GET", "calculateDelivery", orderController::getDeliveryCostForNotRegisteredUser));

        return new ProcessorRequest(placeholders);
    }

    private UserController createUserController(RequestParameterMapper requestParameterMapper, DataSource dataSource) {
        Map<UserRole, String> mapView = Map.of(UserRole.MANAGER, "/manager/managerHome.jsp", UserRole.CLIENT, "/client/clientHome.jsp");
        UserDAO userDAO = new UserDAO(dataSource);
        UserService userService = new UserService(userDAO);
        return new UserController(userService, requestParameterMapper, mapView);
    }

    private ClientController createClientController(RequestParameterMapper requestParameterMapper, DataSource dataSource) {
        ClientDAO clientDAO = new ClientDAO(dataSource);
        ReportFilteringDAO reportFilteringDAO = createReportFilteringDao(dataSource);
        ClientService clientService = new ClientService(clientDAO, reportFilteringDAO);
        PaginationLinksBuilder linksBuilder = new PaginationLinksBuilder();
        return new ClientController(clientService, requestParameterMapper, linksBuilder);
    }

    private OrderController createOrderController(RequestParameterMapper requestParameterMapper, DataSource dataSource) {
        OrderDAO orderDAO = new OrderDAO(dataSource);
        OrderService orderService = new OrderService(orderDAO);
        return new OrderController(orderService, requestParameterMapper);
    }

    private ManagerController createManagerController(RequestParameterMapper requestParameterMapper, DataSource dataSource) {
        ManagerDAO managerDAO = new ManagerDAO(dataSource);
        ReportFilteringDAO reportFilteringDAO = createReportFilteringDao(dataSource);
        ManagerService managerService = new ManagerService(managerDAO, reportFilteringDAO);
        PaginationLinksBuilder linksBuilder = new PaginationLinksBuilder();
        return new ManagerController(managerService,requestParameterMapper, linksBuilder);
    }

    private ReportFilteringDAO createReportFilteringDao(DataSource dataSource){
        List<MapDtoFieldToPreparedStatement> filteringChain = new ArrayList<>();
        filteringChain.add(new MapStringToPreparedStatement());
        filteringChain.add(new MapBooleanToPreparedStatement());
        filteringChain.add(new MapIntegerToPreparedStatement());
        filteringChain.add(new MapLongToPreparedStatement());
        filteringChain.add(new MapDoubleToPreparedStatement());
        PreparerQueryToFiltering preparerQueryToFiltering = new PreparerQueryToFiltering();
        SetterFilteredFieldToPreparedStatement setterFilteredFieldToPreparedStatement = new SetterFilteredFieldToPreparedStatement(filteringChain);
        return new ReportFilteringDAO(dataSource,preparerQueryToFiltering, setterFilteredFieldToPreparedStatement);
    }


}
