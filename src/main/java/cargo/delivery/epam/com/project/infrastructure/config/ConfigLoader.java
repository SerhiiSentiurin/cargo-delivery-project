package cargo.delivery.epam.com.project.infrastructure.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Load configuration database properties into map
 */
@Log4j2
public class ConfigLoader {
    @Getter
    private final Map<String, String> configs;

    public ConfigLoader() {
        this.configs = new HashMap<>();
    }

    /**
     * Load configuration database properties into map
     *
     * @param configPath path to resource file, witch should contain:
     *                   - database URL;
     *                   - database username;
     *                   - database user password
     *                   - path to changeLog file
     */
    public void loadConfigurations(String configPath) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configPath);
            properties.load(inputStream);
            properties.stringPropertyNames()
                    .stream()
                    .iterator()
                    .forEachRemaining(name -> configs.put(name, properties.getProperty(name)));
            log.info("Properties was loaded successfully");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
