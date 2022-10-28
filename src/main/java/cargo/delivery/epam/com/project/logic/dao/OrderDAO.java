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
    public Route getRoute(String senderCity, String recipientCity) {
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
        String sql = "select distinct sender_city from route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);) {
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
        String sql = "select distinct recipient_city from route";
        List<Route> routesList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);) {
            while (resultSet.next()) {
                String recipientCity = resultSet.getString("recipient_city");
                Route route = new Route();
                route.setRecipientCity(recipientCity);
                routesList.add(route);
            }
        }
        return routesList;
    }

//    @SneakyThrows
//    public void createOrder(ClientOrderDto dto) {
//        String insertIntoInvoice = "insert into invoice (price) value (?)"; // dto
//        String insertIntoDelivery = "insert into delivery (route_id) value ((select id from route where sender_city = ? and recipient_city = ?))"; //dto
//        String insertIntoOrder = "insert into orders (type, weight, volume, delivery_id, invoice_id) values (?, ?, ?, ?, ?)"; // dto,dto,dto, KEYS_delivery, KEYS_invoice
//        String insertIntoReport = "insert into report (client_id, order_id) values(?, ?)"; // dto, KEYS_order
//        Connection connection = null;
//        PreparedStatement preparedStatementInvoice = null;
//        PreparedStatement preparedStatementDelivery = null;
//        PreparedStatement preparedStatementOrder = null;
//        PreparedStatement preparedStatementReport = null;
//        ResultSet resultSetInvoice = null;
//        ResultSet resultSetDelivery = null;
//        ResultSet resultSetOrder = null;
//        long deliveryId = 0L;
//        long invoiceId = 0L;
//        long orderId = 0L;
//        try {
//            connection = dataSource.getConnection();
//            connection.setAutoCommit(false);
//            preparedStatementInvoice = connection.prepareStatement(insertIntoInvoice, Statement.RETURN_GENERATED_KEYS);
//            preparedStatementInvoice.setDouble(1, dto.getDeliveryCost());
//            preparedStatementInvoice.execute();
//            resultSetInvoice = preparedStatementInvoice.getGeneratedKeys();
//            if (resultSetInvoice.next()) {
//                invoiceId = resultSetInvoice.getLong(1);
//            }
//
//            preparedStatementDelivery = connection.prepareStatement(insertIntoDelivery, Statement.RETURN_GENERATED_KEYS);
//            preparedStatementDelivery.setString(1, dto.getSenderCity());
//            preparedStatementDelivery.setString(2, dto.getRecipientCity());
//            preparedStatementDelivery.execute();
//            resultSetDelivery = preparedStatementDelivery.getGeneratedKeys();
//            if (resultSetDelivery.next()) {
//                deliveryId = resultSetDelivery.getLong(1);
//            }
//
//            preparedStatementOrder = connection.prepareStatement(insertIntoOrder, Statement.RETURN_GENERATED_KEYS);
//            preparedStatementOrder.setString(1, dto.getType());
//            preparedStatementOrder.setDouble(2, dto.getWeight());
//            preparedStatementOrder.setDouble(3, dto.getVolume());
//            preparedStatementOrder.setLong(4, deliveryId);
//            preparedStatementOrder.setLong(5, invoiceId);
//            preparedStatementOrder.execute();
//            resultSetOrder = preparedStatementOrder.getGeneratedKeys();
//            if (resultSetOrder.next()) {
//                orderId = resultSetOrder.getLong(1);
//            }
//
//            preparedStatementReport = connection.prepareStatement(insertIntoReport);
//            preparedStatementReport.setLong(1, dto.getClientId());
//            preparedStatementReport.setLong(2, orderId);
//            preparedStatementReport.execute();
//
//            connection.commit();
//        } catch (Exception e) {
//            rollback(connection);
//            log.error(e.getMessage());
//            throw new AppException("Cannot create order!");
//        } finally {
//            close(resultSetInvoice);
//            close(resultSetDelivery);
//            close(resultSetOrder);
//            close(preparedStatementInvoice);
//            close(preparedStatementDelivery);
//            close(preparedStatementOrder);
//            close(preparedStatementReport);
//            close(connection);
//        }
//    }

    @SneakyThrows
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

    @SneakyThrows
    private Long insertIntoInvoice(Connection connection, ClientOrderDto dto) {
        String insertIntoInvoice = "insert into invoice (price) value (?)"; // dto
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

    @SneakyThrows
    private Long insertIntoDelivery(Connection connection, ClientOrderDto dto) {
        String insertIntoDelivery = "insert into delivery (route_id) value ((select id from route where sender_city = ? and recipient_city = ?))"; //dto
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

    @SneakyThrows
    private Long insertIntoOrder(Connection connection, ClientOrderDto dto) {
        String insertIntoOrder = "insert into orders (type, weight, volume, delivery_id, invoice_id) values (?, ?, ?, ?, ?)"; // dto,dto,dto, KEYS_delivery, KEYS_invoice
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

    @SneakyThrows
    private void insertIntoReport(Connection connection, ClientOrderDto dto) {
        String insertIntoReport = "insert into report (client_id, order_id) values(?, ?)"; // dto, KEYS_order
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
