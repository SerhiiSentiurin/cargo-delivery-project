package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.dao.filtering.SetterFilteredFieldToPreparedStatement;
import cargo.delivery.epam.com.project.logic.dao.filtering.PreparerQueryToFiltering;
import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is DAO-layer class. Uses for get data from database and build entity object.
 */
@RequiredArgsConstructor
public class ReportFilteringDAO {
    private final DataSource dataSource;
    private final PreparerQueryToFiltering preparerQuery;
    private final SetterFilteredFieldToPreparedStatement setterFilteredFieldToPreparedStatement;

    /**
     * Gets from database needed data to build entity object with filtering criteria.
     * Delegates building SQL query to PreparerQueryToFiltering.
     * Delegates work with PreparedStatement to SetterFilteredFieldToPreparedStatement.
     * If the filtering field was not selected by the user - it does not participate in filtering.
     *
     * @param dto data-transfer-object which contain all needed fields to filtering.
     * @return list of Reports with chosen filters by user.
     * @see PreparerQueryToFiltering
     * @see SetterFilteredFieldToPreparedStatement
     * @see Report
     */
    @SneakyThrows
    public List<Report> filterReports(FilteringDto dto) {
        List<Report> reportList = new ArrayList<>();
        String sqlQueryToFiltering = preparerQuery.buildCheckedQueryToFiltering(dto);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQueryToFiltering)) {
            setterFilteredFieldToPreparedStatement.setFieldsFromDtoToPreparedStatement(preparedStatement, dto);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long clientId = resultSet.getLong("client.id");
                Long orderId1 = resultSet.getLong("orders.id");
                client.setId(clientId);
                order.setId(orderId1);
                report.setClient(client);
                report.setOrder(order);
                reportList.add(report);
            }
        }
        return reportList;
    }

    @SneakyThrows
    public Client getClientById(Long clientId) {
        String sql = "SELECT amount, login FROM user JOIN client ON user.id = client.id WHERE client.id = ?";
        Client client = new Client();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(clientId);
                client.setAmount(resultSet.getDouble("amount"));
                client.setLogin(resultSet.getString("login"));
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

    @SneakyThrows
    public Delivery getDeliveryById(Long deliveryId) {
        String sql = "SELECT * FROM delivery WHERE id = ?";
        Delivery delivery = new Delivery();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, deliveryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                delivery.setId(deliveryId);
                if (resultSet.getDate("departure_date") != null && resultSet.getDate("arrival_date") != null) {
                    delivery.setDepartureDate(resultSet.getDate("departure_date").toLocalDate());
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
                Invoice invoice = getInvoiceById(resultSet.getLong("invoice_id"));
                order.setDelivery(delivery);
                order.setInvoice(invoice);
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

    /**
     * Gets the number of rows from BD according to filtering criteria (for pagination).
     * Delegates building SQL query to PreparerQueryToFiltering.
     * Delegates work with PreparedStatement to SetterFilteredFieldToPreparedStatement.
     *
     * @param dto data-transfer-object with filtering criteria (fields)
     * @return number of filtered rows
     * @see PreparerQueryToFiltering
     * @see SetterFilteredFieldToPreparedStatement
     */
    @SneakyThrows
    public double getCountFilteredRows(FilteringDto dto) {

        String queryCountOfFilteredRows = preparerQuery.buildCheckedQueryToCountRows(dto);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryCountOfFilteredRows)) {
            dto.setPage(null);
            setterFilteredFieldToPreparedStatement.setFieldsFromDtoToPreparedStatement(preparedStatement, dto);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
            return 0;
        }
    }
}
