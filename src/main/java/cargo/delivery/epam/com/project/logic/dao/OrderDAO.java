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

//    @SneakyThrows
//    public List<Route> getAllRoutes() {
//        String sql = "SELECT * FROM route";
//        List<Route> routesList = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql);) {
//            while (resultSet.next()) {
//                 long id = resultSet.getLong("id");
//                 double distance = resultSet.getDouble("distance");
//                 String senderCity = resultSet.getString("sender_city");
//                 String recipientCity = resultSet.getString("recipient_city");
//                 Route route = new Route(id,distance,senderCity,recipientCity);
//                 routesList.add(route);
//            }
//        }
//        return routesList;
//    }

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

    @SneakyThrows
    public void createOrder(ClientOrderDto dto) {
        String insertIntoDelivery = "insert into delivery (route_id) value ((select id from route where sender_city = ? and recipient_city = ?))"; // from (dto)
        String insertIntoOrder = "insert into orders (type,weight,volume, delivery_id) values (?, ?, ?, ?)"; // from (dto, dto, dto, GENERATED_KEYS_delivery)
        String insertIntoInvoice = "insert into invoice (price) value (?)"; // from (dto)
        String insertIntoClient = "update client set order_id = ?, invoice_id = ? where id = ?"; // from (GENERATED_KEYS_orders, GENERATED_KEYS_invoice, dto)
        String insertIntoReport = "insert into report (client_id, delivery_id) values(?,?)"; // from (dto, GENERATED_KEYS_delivery)

        Connection connection = null;
        PreparedStatement preparedStatementDelivery = null;
        PreparedStatement preparedStatementOrder = null;
        PreparedStatement preparedStatementInvoice = null;
        PreparedStatement preparedStatementClient = null;
        PreparedStatement preparedStatementReport = null;
        ResultSet resultSetDelivery = null;
        ResultSet resultSetOrder = null;
        ResultSet resultSetInvoice = null;

        long deliveryId = 0L;
        long orderId = 0L;
        long invoiceId = 0L;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatementDelivery = connection.prepareStatement(insertIntoDelivery, Statement.RETURN_GENERATED_KEYS);
            preparedStatementDelivery.setString(1, dto.getSenderCity());
            preparedStatementDelivery.setString(2, dto.getRecipientCity());
            preparedStatementDelivery.execute();
            resultSetDelivery = preparedStatementDelivery.getGeneratedKeys();
            if (resultSetDelivery.next()) {
                deliveryId = resultSetDelivery.getLong(1);
            }

            preparedStatementOrder = connection.prepareStatement(insertIntoOrder, Statement.RETURN_GENERATED_KEYS);
            preparedStatementOrder.setString(1, dto.getType());
            preparedStatementOrder.setDouble(2, dto.getWeight());
            preparedStatementOrder.setDouble(3, dto.getVolume());
            preparedStatementOrder.setLong(4, deliveryId);
            preparedStatementOrder.execute();
            resultSetOrder = preparedStatementOrder.getGeneratedKeys();
            if (resultSetOrder.next()) {
                orderId = resultSetOrder.getLong(1);
            }

            preparedStatementInvoice = connection.prepareStatement(insertIntoInvoice, Statement.RETURN_GENERATED_KEYS);
            preparedStatementInvoice.setDouble(1, dto.getDeliveryCost());
            preparedStatementInvoice.execute();
            resultSetInvoice = preparedStatementInvoice.getGeneratedKeys();
            if (resultSetInvoice.next()) {
                invoiceId = resultSetInvoice.getLong(1);
            }

            preparedStatementClient = connection.prepareStatement(insertIntoClient);
            preparedStatementClient.setLong(1, orderId);
            preparedStatementClient.setLong(2, invoiceId);
            preparedStatementClient.setLong(3, dto.getClientId());
            preparedStatementClient.execute();

            preparedStatementReport = connection.prepareStatement(insertIntoReport);
            preparedStatementReport.setLong(1, dto.getClientId());
            preparedStatementReport.setLong(2, deliveryId);
            preparedStatementReport.execute();

            connection.commit();
        } catch (Exception exception) {
            rollback(connection);
            log.error(exception.getMessage());
            throw  new AppException("Cannot create order!");
        }finally {
            close(resultSetDelivery);
            close(resultSetOrder);
            close(resultSetInvoice);
            close(preparedStatementDelivery);
            close(preparedStatementOrder);
            close(preparedStatementInvoice);
            close(preparedStatementClient);
            close(preparedStatementReport);
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
}
