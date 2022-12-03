package cargo.delivery.epam.com.project.infrastructure.config.db;

import cargo.delivery.epam.com.project.infrastructure.config.ConfigLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

/**
 * Creates connection pool (Hikari connection pool) from database properties
 *
 * @see ConfigLoader
 */
@Log4j2
public class ConfigDataSource {
    public DataSource createDataSource(ConfigLoader configLoader) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configLoader.getConfigs().get("db.url"));
        hikariConfig.setUsername(configLoader.getConfigs().get("db.username"));
        hikariConfig.setPassword(configLoader.getConfigs().get("db.password"));
        log.info("connection pool created");
        return new HikariDataSource(hikariConfig);
    }
}
