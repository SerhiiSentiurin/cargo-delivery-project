package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Log4j2
@RequiredArgsConstructor
public class ClientDAO {
    private final DataSource dataSource;

    public void createNewClient(ClientCreateDto dto) {
        String insertIntoUser = "INSERT INTO user (login, password, role) VALUES (?,?,?)";
        String insertIntoClient = "INSERT INTO client (id, amount) VALUES(?, 0)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(insertIntoUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dto.getLogin());
            preparedStatement.setString(2, dto.getPassword());
            preparedStatement.setString(3, dto.getUserRole().toString());
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long clientId = resultSet.getLong(1);
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(insertIntoClient)) {
                        preparedStatement1.setLong(1, clientId);
                        preparedStatement1.execute();
                        connection.commit();
                    }
                }
            }
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new AppException("Cannot create reader! Try to insert another login or password");
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }


    @SneakyThrows
    private void rollback(Connection connection) {
        if (connection != null)
            connection.rollback();
    }

    @SneakyThrows
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    @SneakyThrows
    private void insertIntoNewClientZeroAmount(Long id) {
        String insertIntoClient = "INSERT INTO client (id, amount) VALUES(?, 0)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertIntoClient, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        }
    }
}
