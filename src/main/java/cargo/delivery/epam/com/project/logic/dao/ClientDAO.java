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

    public void createNewClient(ClientCreateDto dto) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            insertIntoClient(connection, dto);
            connection.commit();
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new AppException("Cannot create client! Try to insert another login or password");
        } finally {
            close(connection);
        }
    }

    protected Long insertIntoUser(Connection connection, ClientCreateDto dto) {
        String insertIntoUser = "INSERT INTO user (login, password, role) VALUES (?,?,?)";
        PreparedStatement preparedStatementUser = null;
        ResultSet resultSetUser = null;
        try {
            preparedStatementUser = connection.prepareStatement(insertIntoUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatementUser.setString(1, dto.getLogin());
            preparedStatementUser.setString(2, dto.getPassword());
            preparedStatementUser.setString(3, dto.getUserRole().toString());
            preparedStatementUser.execute();
            resultSetUser = preparedStatementUser.getGeneratedKeys();
            if (resultSetUser.next()) {
                return resultSetUser.getLong(1);
            }
            return 0L;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot insert User!");
        } finally {
            close(resultSetUser);
            close(preparedStatementUser);
        }
    }

    protected void insertIntoClient(Connection connection, ClientCreateDto dto) {
        String insertIntoClient = "INSERT INTO client (id, amount) VALUES(?, 0)";
        Long clientId = insertIntoUser(connection, dto);
        PreparedStatement preparedStatementClient = null;
        try {
            preparedStatementClient = connection.prepareStatement(insertIntoClient);
            preparedStatementClient.setLong(1, clientId);
            preparedStatementClient.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot insert Client!");
        } finally {
            close(preparedStatementClient);
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
    public List<Report> getReportsByClientId(Long clientId, int index) {
        String sql = "select client_id, order_id from report join orders on report.order_id=orders.id join invoice on orders.invoice_id=invoice.id where client_id = ? order by isConfirmed asc, isPaid asc, order_id desc limit ?, 10";
        List<Report> clientOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            preparedStatement.setInt(2, index);
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

    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate, Double amountAfterPaid) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            updateDelivery(connection, orderId, departureDate, arrivalDate);
            updateInvoice(connection, orderId);
            updateClient(connection, clientId, amountAfterPaid);
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
    private void updateClient(Connection connection, Long clientId, Double amountAfterPaid) {
        String updateClient = "update client set amount = ? where id = ?";
        try (PreparedStatement preparedStatementClient = connection.prepareStatement(updateClient)) {
            preparedStatementClient.setDouble(1, amountAfterPaid);
            preparedStatementClient.setLong(2, clientId);
            preparedStatementClient.execute();
        }
    }

    @SneakyThrows
    public double getCountOfRowsAllOrdersClient(Long clientId) {
        String sql = "select count(*) from report where client_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }
        return 0;

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
