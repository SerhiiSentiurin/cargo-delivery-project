package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ClientDAO {
    private final DataSource dataSource;

    // divide at several methods!!!
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
    public void topUpClientWallet(Double amountForTopUp, Long clientId, Double clientAmount) {
        String sql = "UPDATE client SET amount = ? where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amountForTopUp + clientAmount);
            preparedStatement.setLong(2, clientId);
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    public List<Report> getReportsByClientId(Long clientId) {
        String sql = "select client_id, order_id from report join orders on report.order_id=orders.id join invoice on orders.invoice_id=invoice.id where client_id = ? order by isConfirmed asc, isPaid asc, order_id desc";
        List<Report> clientOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long orderId1 = resultSet.getLong("order_id");
                client.setId(clientId);
                order.setId(orderId1);
                report.setClient(client);
                report.setOrder(order);
                clientOrders.add(report);
            }
        }
        return clientOrders;
    }

    @SneakyThrows
    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate, Double amountAfterPaid) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            updateDelivery(connection, orderId, departureDate, arrivalDate);
            updateInvoice(connection, orderId);
            updateClient(connection, orderId, clientId, amountAfterPaid);
            connection.commit();
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new AppException("Transaction failed with paid operation!");
        } finally {
            close(connection);
        }
    }

    @SneakyThrows
    private void updateDelivery(Connection connection, Long orderId, LocalDate departureDate, LocalDate arrivalDate) {
        String updateDelivery = "update delivery join orders on delivery.id = orders.delivery_id set departure_date = ?, arrival_date = ? where orders.id = ?";
        try (PreparedStatement preparedStatementDelivery = connection.prepareStatement(updateDelivery)) {
            preparedStatementDelivery.setDate(1, Date.valueOf(departureDate));
            preparedStatementDelivery.setDate(2, Date.valueOf(arrivalDate));
            preparedStatementDelivery.setLong(3, orderId);
            preparedStatementDelivery.execute();
        }
    }

    @SneakyThrows
    private void updateInvoice(Connection connection, Long orderId) {
        String updateInvoice = "update invoice join orders on invoice.id = orders.invoice_id set isPaid=true where orders.id = ?";
        try (PreparedStatement preparedStatementInvoice = connection.prepareStatement(updateInvoice)) {
            preparedStatementInvoice.setLong(1, orderId);
            preparedStatementInvoice.execute();
        }
    }

    @SneakyThrows
    private void updateClient(Connection connection, Long orderId, Long clientId, Double amountAfterPaid) {
        String updateClient = "update client set amount = ? where id = ?";
        try (PreparedStatement preparedStatementClient = connection.prepareStatement(updateClient)) {
            preparedStatementClient.setDouble(1, amountAfterPaid);
            preparedStatementClient.setLong(2, clientId);
            preparedStatementClient.execute();
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

}
