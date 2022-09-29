package cargo.delivery.epam.com.project.infrastructure.config.servlet;

import cargo.delivery.epam.com.project.infrastructure.config.ConfigLoader;
import cargo.delivery.epam.com.project.infrastructure.config.db.ConfigDataSource;
import cargo.delivery.epam.com.project.infrastructure.config.db.ConfigLiquibase;
import cargo.delivery.epam.com.project.infrastructure.web.*;
import cargo.delivery.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import cargo.delivery.epam.com.project.logic.controllers.ClientController;
import cargo.delivery.epam.com.project.logic.controllers.OrderController;
import cargo.delivery.epam.com.project.logic.controllers.UserController;
import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.OrderDAO;
import cargo.delivery.epam.com.project.logic.dao.UserDAO;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import cargo.delivery.epam.com.project.logic.services.ClientService;
import cargo.delivery.epam.com.project.logic.services.OrderService;
import cargo.delivery.epam.com.project.logic.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import liquibase.pro.packaged.P;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
public class FrontServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        // add listener

        FrontServlet frontServlet = createFrontServlet();
        ServletRegistration.Dynamic dynamic = ctx.addServlet("frontServlet", frontServlet);
        dynamic.setLoadOnStartup(0);
        dynamic.addMapping("/cargo/*");
        log.info("Front Servlet was started");

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

        UserController userController = createUserController(requestParameterMapper,dataSource);
        ClientController clientController = createClientController(requestParameterMapper, dataSource);
        OrderController orderController = createOrderController(requestParameterMapper,dataSource);


        placeholders.add(new Placeholder("POST", "login", userController::login));
        placeholders.add(new Placeholder("POST", "logout", userController::logout));
        placeholders.add(new Placeholder("POST","client/create",clientController::createNewClient));
        placeholders.add(new Placeholder("GET", "chooseCargoParameters", orderController::getAllRoutes));
        placeholders.add(new Placeholder("GET", "calculateDelivery", orderController::getDeliveryCost));

        return new ProcessorRequest(placeholders);
    }

    private UserController createUserController(RequestParameterMapper requestParameterMapper, DataSource dataSource){
        Map<UserRole, String> mapView = Map.of(UserRole.MANAGER,"/manager/managerHome.jsp", UserRole.CLIENT, "/client/clientHome.jsp");
        UserDAO userDAO = new UserDAO(dataSource);
        UserService userService = new UserService(userDAO);
        return new UserController(userService,requestParameterMapper,mapView);
    }

    private ClientController createClientController(RequestParameterMapper requestParameterMapper, DataSource dataSource){
        ClientDAO clientDAO = new ClientDAO(dataSource);
        ClientService clientService = new ClientService(clientDAO);
        return new ClientController(clientService,requestParameterMapper);
    }

    private OrderController createOrderController(RequestParameterMapper requestParameterMapper, DataSource dataSource){
        OrderDAO orderDAO = new OrderDAO(dataSource);
        OrderService orderService = new OrderService(orderDAO);
        return new OrderController(orderService,requestParameterMapper);
    }


}
