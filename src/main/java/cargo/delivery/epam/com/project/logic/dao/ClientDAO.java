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
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
            }
        }
        return client;
    }

    @SneakyThrows
    public List<Order> getOrdersByClientId(Long clientId) {
        String sql = "select orders.id, type, weight, volume, delivery_id, invoice_id, isConfirmed from orders join report on orders.id= report.order_id join invoice on orders.invoice_id=invoice.id where client_id = ? order by isPaid, isConfirmed";
        List<Order> clientOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long orderId = resultSet.getLong("orders.id");
                String type = resultSet.getString("type");
                Double weight = resultSet.getDouble("weight");
                Double volume = resultSet.getDouble("volume");
                Long deliveryId = resultSet.getLong("delivery_id");
                Long invoiceId = resultSet.getLong("invoice_id");
                Boolean isConfirmed = resultSet.getBoolean("isConfirmed");
                Delivery delivery = getDeliveryById(deliveryId);
                Invoice invoice = getInvoiceById(invoiceId);
                Order order = new Order(orderId,type,weight,volume,delivery,invoice,isConfirmed);
                clientOrders.add(order);
            }
        }
        return clientOrders;
    }

    @SneakyThrows
    public Order getOrderById(Long orderId){
        String sql = "select * from orders where id = ?";
        Order order = new Order();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                order.setId(orderId);
                order.setType(resultSet.getString("type"));
                order.setWeight(resultSet.getDouble("weight"));
                order.setVolume(resultSet.getDouble("volume"));
                order.setIsConfirmed(resultSet.getBoolean("isConfirmed"));
                Delivery delivery = getDeliveryById(resultSet.getLong("delivery_id"));
                Invoice invoice = getInvoiceById(resultSet.getLong("invoice_id"));
                order.setDelivery(delivery);
                order.setInvoice(invoice);
            }
        }
        return order;
    }

    @SneakyThrows
    public Order getOrderForInvoice(Long clientId, Long orderId){
        String sql = "select orders.id, type, weight,volume, delivery_id, invoice_id, isConfirmed from report join orders on report.order_id=orders.id where report.client_id = ? and report.order_id = ?";
        Order order = new Order();
        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, clientId);
            preparedStatement.setLong(2, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                order.setId(orderId);
                order.setType(resultSet.getString("type"));
                order.setWeight(resultSet.getDouble("weight"));
                order.setVolume(resultSet.getDouble("volume"));
                order.setIsConfirmed(resultSet.getBoolean("isConfirmed"));
                Delivery delivery = getDeliveryById(resultSet.getLong("delivery_id"));
                Invoice invoice = getInvoiceById(resultSet.getLong("invoice_id"));
                order.setDelivery(delivery);
                order.setInvoice(invoice);
            }
        }
        return order;
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

    // divide in three methods ( send connection as a parameter)
    @SneakyThrows
    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate){
        String updateDelivery = "update delivery join orders on delivery.id = orders.delivery_id set departure_date = ?, arrival_date = ? where orders.id = ?";
        String updateInvoice = "update invoice join orders on invoice.id = orders.invoice_id set isPaid=true where orders.id = ?";
        String updateClient = "update client set amount = ? where id = ?";
        Double amountAfterPaid = checkWalletAmount(clientId,orderId);
        Connection connection = null;
        PreparedStatement preparedStatementDelivery = null;
        PreparedStatement preparedStatementInvoice = null;
        PreparedStatement preparedStatementClient = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatementDelivery = connection.prepareStatement(updateDelivery);
            preparedStatementDelivery.setDate(1, Date.valueOf(departureDate));
            preparedStatementDelivery.setDate(2, Date.valueOf(arrivalDate));
            preparedStatementDelivery.setLong(3, orderId);
            preparedStatementDelivery.execute();

            preparedStatementInvoice = connection.prepareStatement(updateInvoice);
            preparedStatementInvoice.setLong(1, orderId);
            preparedStatementInvoice.execute();

            preparedStatementClient = connection.prepareStatement(updateClient);
            preparedStatementClient.setDouble(1, amountAfterPaid);
            preparedStatementClient.setLong(2, clientId);
            preparedStatementClient.execute();

            connection.commit();
        }catch (Exception e){
            rollback(connection);
            log.error(e.getMessage());
            throw new AppException("Transaction failed with paid operation!");
        }finally {
            close(preparedStatementDelivery);
            close(preparedStatementInvoice);
            close(preparedStatementClient);
            close(connection);
        }


    }

    private Double checkWalletAmount(Long clientId, Long orderId){
        Client client = getClientById(clientId);
        Order order = getOrderById(orderId);
        Double clientAmount = client.getAmount();
        Double orderCost = order.getInvoice().getPrice();
        if (clientAmount >= orderCost) {
            return clientAmount - orderCost;
        }else {
            throw new AppException("Not enough money!");
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
