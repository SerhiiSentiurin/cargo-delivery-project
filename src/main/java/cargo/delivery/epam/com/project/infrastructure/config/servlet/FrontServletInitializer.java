package cargo.delivery.epam.com.project.infrastructure.config.servlet;

import cargo.delivery.epam.com.project.infrastructure.config.ConfigLoader;
import cargo.delivery.epam.com.project.infrastructure.web.*;
import cargo.delivery.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log4j2
public class FrontServletInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        // add listener

        FrontServlet frontServlet = createFrontServlet();
        ServletRegistration.Dynamic dynamic = ctx.addServlet(frontServlet.getServletName(),frontServlet);
        dynamic.setLoadOnStartup(0);
        dynamic.addMapping("/cargo/*");
        log.info("Front Servlet was started");

    }

    private FrontServlet createFrontServlet(){
        ProcessorRequest processorRequest = createProcessorRequest();
        ProcessorModelAndView processorModelAndView = new ProcessorModelAndView();
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        return new FrontServlet("frontServlet",processorRequest,processorModelAndView,exceptionHandler);
    }

    private ProcessorRequest createProcessorRequest(){
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.loadConfigurations("app.yaml");
        List<Placeholder>placeholders = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestParameterMapper requestParameterMapper = new RequestParameterMapper(objectMapper);



        return null;
    }


}
