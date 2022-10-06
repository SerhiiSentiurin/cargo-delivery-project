package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.*;

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
    public void topUpClientWallet(Double amount, Long clientId) {
        String sql = "UPDATE client SET amount = ? where id = ?";
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setDouble(1, amount + getClientById(clientId).getAmount());
            preparedStatement.setLong(2, clientId);
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    public Client getClientById(Long clientId) {
        String sql = "SELECT * FROM client WHERE id = ?";
        Client client = new Client();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(clientId);
                client.setAmount(resultSet.getDouble("amount"));
                Long orderId = resultSet.getLong("order_id");
                Long invoiceId = resultSet.getLong("invoice_id");
                client.setOrder(getOrderById(orderId));
                client.setInvoice(getInvoiceById(invoiceId));
            }
        }
        return client;
    }

    @SneakyThrows
    public Route getRouteById(Long routeId) {
        String sql = "SELECT * FROM route WHERE id = ?";
        Route route = new Route();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                route.setId(routeId);
                route.setDistance(resultSet.getDouble("distance"));
                route.setSenderCity(resultSet.getString("sender_city"));
                route.setRecipientCity(resultSet.getString("recipient_city"));
            }
        }
        return route;
    }


//          cannot set null in LocalDate????????
//    @SneakyThrows
//    public Delivery getDeliveryById(Long deliveryId) {
//        String sql = "SELECT * FROM delivery where id = ?";
//        Delivery delivery = new Delivery();
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setLong(1, deliveryId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                delivery.setId(deliveryId);
//                Optional<Date> departureDate = Optional.of(resultSet.getDate("departure_date"));
//                Optional<Date> arrivalDate = Optional.of(resultSet.getDate("arrival_date"));
//                delivery.setDepartureDate(departureDate.get().toLocalDate());
//                delivery.setArrivingDate(arrivalDate.get().toLocalDate());
//                Route route = getRouteById(resultSet.getLong("route_id"));
//                delivery.setRoute(route);
//            }
//        }
//        return delivery;
//    }

    @SneakyThrows
    public Delivery getDeliveryById(Long deliveryId) {
        String sql = "SELECT * FROM delivery where id = ?";
        Delivery delivery = new Delivery();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, deliveryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                delivery.setId(deliveryId);
                if (resultSet.getDate("departure_date") == null) {
                    delivery.setDepartureDate(null);
                } else {
                    delivery.setDepartureDate(resultSet.getDate("departure_date").toLocalDate());
                }

                if (resultSet.getDate("arrival_date") == null) {
                    delivery.setArrivalDate(null);
                } else {
                    delivery.setArrivalDate(resultSet.getDate("arrival_date").toLocalDate());
                }
                Route route = getRouteById(resultSet.getLong("route_id"));
                delivery.setRoute(route);
            }
        }
        return delivery;
    }

    @SneakyThrows
    public Order getOrderById(Long orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Order order = new Order();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                order.setId(orderId);
                order.setType(resultSet.getString("type"));
                order.setWeight(resultSet.getDouble("weight"));
                order.setVolume(resultSet.getDouble("volume"));
                order.setIsConfirmed(resultSet.getBoolean("isConfirmed"));
                Delivery delivery = getDeliveryById(resultSet.getLong("delivery_id"));
                order.setDelivery(delivery);
            }
        }
        return order;
    }

    @SneakyThrows
    public Invoice getInvoiceById(Long invoiceId) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        Invoice invoice = new Invoice();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, invoiceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                invoice.setId(invoiceId);
                invoice.setPrice(resultSet.getDouble("price"));
                invoice.setIsPaid(resultSet.getBoolean("isPaid"));
            }
        }
        return invoice;
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
