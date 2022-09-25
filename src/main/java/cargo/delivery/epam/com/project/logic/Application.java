package cargo.delivery.epam.com.project.logic;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Log4j2
public class Application {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.addWebapp("/app", new File("webapp").getAbsolutePath());
        tomcat.getConnector();
        tomcat.start();
        log.info("start application --> " + tomcat);
        tomcat.getServer().await();
    }
}
