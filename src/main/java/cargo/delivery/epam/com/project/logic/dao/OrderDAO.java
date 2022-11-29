package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class OrderDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public Route getRouteByCities(String senderCity, String recipientCity) {
        String sql = "SELECT * FROM route WHERE sender_city = ? AND recipient_city = ?";
        Route route = new Route();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, senderCity);
            preparedStatement.setString(2, recipientCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                route.setId(resultSet.getLong("id"));
                route.setDistance(resultSet.getDouble("distance"));
                route.setSenderCity(resultSet.getString("sender_city"));
                route.setRecipientCity(resultSet.getString("recipient_city"));
            }
        }
        return route;
    }

    @SneakyThrows
    public List<Route> getDistinctSenderCities() {
        String sql = "SELECT DISTINCT sender_city FROM route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String senderCity = resultSet.getString("sender_city");
                Route route = new Route();
                route.setSenderCity(senderCity);
                routesList.add(route);
            }
        }
        return routesList;
    }

    @SneakyThrows
    public List<Route> getDistinctRecipientCities() {
        String sql = "SELECT DISTINCT recipient_city FROM route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String recipientCity = resultSet.getString("recipient_city");
                Route route = new Route();
                route.setRecipientCity(recipientCity);
                routesList.add(route);
            }
        }
        return routesList;
    }

    public void createOrder(ClientOrderDto dto) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            insertIntoReport(connection, dto);
            connection.commit();
        } catch (Exception e) {
            rollback(connection);
            log.error(e.getMessage());
            throw new AppException("Cannot create order!");
        } finally {
            close(connection);
        }
    }

    private Long insertIntoInvoice(Connection connection, ClientOrderDto dto) {
        String insertIntoInvoice = "INSERT INTO invoice (price) VALUE (?)";
        PreparedStatement preparedStatementInvoice = null;
        ResultSet resultSetInvoice = null;
        try {
            preparedStatementInvoice = connection.prepareStatement(insertIntoInvoice, Statement.RETURN_GENERATED_KEYS);
            preparedStatementInvoice.setDouble(1, dto.getDeliveryCost());
            preparedStatementInvoice.execute();
            resultSetInvoice = preparedStatementInvoice.getGeneratedKeys();
            if (resultSetInvoice.next()) {
                return resultSetInvoice.getLong(1);
            }
            return 0L;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot create order!");
        } finally {
            close(resultSetInvoice);
            close(preparedStatementInvoice);
        }
    }

    private Long insertIntoDelivery(Connection connection, ClientOrderDto dto) {
        String insertIntoDelivery = "INSERT INTO delivery (route_id) VALUE ((SELECT id FROM route WHERE sender_city = ? AND recipient_city = ?))";
        PreparedStatement preparedStatementDelivery = null;
        ResultSet resultSetDelivery = null;
        try {
            preparedStatementDelivery = connection.prepareStatement(insertIntoDelivery, Statement.RETURN_GENERATED_KEYS);
            preparedStatementDelivery.setString(1, dto.getSenderCity());
            preparedStatementDelivery.setString(2, dto.getRecipientCity());
            preparedStatementDelivery.execute();
            resultSetDelivery = preparedStatementDelivery.getGeneratedKeys();
            if (resultSetDelivery.next()) {
                return resultSetDelivery.getLong(1);
            }
            return 0L;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot create order!");
        } finally {
            close(resultSetDelivery);
            close(preparedStatementDelivery);
        }
    }

    private Long insertIntoOrder(Connection connection, ClientOrderDto dto) {
        String insertIntoOrder = "INSERT INTO orders (type, weight, volume, delivery_id, invoice_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatementOrder = null;
        ResultSet resultSetOrder = null;
        Long invoiceId = insertIntoInvoice(connection, dto);
        Long deliveryId = insertIntoDelivery(connection, dto);
        try {
            preparedStatementOrder = connection.prepareStatement(insertIntoOrder, Statement.RETURN_GENERATED_KEYS);
            preparedStatementOrder.setString(1, dto.getType());
            preparedStatementOrder.setDouble(2, dto.getWeight());
            preparedStatementOrder.setDouble(3, dto.getVolume());
            preparedStatementOrder.setLong(4, deliveryId);
            preparedStatementOrder.setLong(5, invoiceId);
            preparedStatementOrder.execute();
            resultSetOrder = preparedStatementOrder.getGeneratedKeys();
            if (resultSetOrder.next()) {
                return resultSetOrder.getLong(1);
            }
            return 0L;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot create order!");
        } finally {
            close(resultSetOrder);
            close(preparedStatementOrder);
        }
    }

    private void insertIntoReport(Connection connection, ClientOrderDto dto) {
        String insertIntoReport = "INSERT INTO report (client_id, order_id) VALUES (?, ?)";
        PreparedStatement preparedStatementReport = null;
        Long orderId = insertIntoOrder(connection, dto);
        try {
            preparedStatementReport = connection.prepareStatement(insertIntoReport);
            preparedStatementReport.setLong(1, dto.getClientId());
            preparedStatementReport.setLong(2, orderId);
            preparedStatementReport.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException("Cannot create order!");
        } finally {
            close(preparedStatementReport);
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
