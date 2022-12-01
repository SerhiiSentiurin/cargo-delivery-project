package cargo.delivery.epam.com.project.infrastructure.config.db;

import cargo.delivery.epam.com.project.infrastructure.config.ConfigLoader;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Update database with every application launch.
 */
@Log4j2
@RequiredArgsConstructor
public class ConfigLiquibase {

    private final DataSource dataSource;

    /**
     * Update database with every application launch.
     * If you want to change DB schema or insert some data in DB you should create new changeLog file at
     * "src/main/resources/db/liquibase" and include this file into "changelog-master.yaml" file.
     *
     * @param configLoader this is map with DB properties (URL, username, user password, path to changeLog file)
     * @see ConfigLoader
     */
    @SneakyThrows
    public void updateDatabase(ConfigLoader configLoader) {
        String change_log_file = configLoader.getConfigs().get("change_log_file");
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase(change_log_file, new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());

            log.info("Update database");
        }
    }
}
