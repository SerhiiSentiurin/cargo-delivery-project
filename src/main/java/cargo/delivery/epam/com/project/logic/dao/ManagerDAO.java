package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ManagerDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public double getCountOfRowsAllOrders() {
        String sql = "SELECT count(*) FROM report";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
        }
        return 0;
    }

    @SneakyThrows
    public List<Report> getAllOrders(int index) {
        String sql = "SELECT client_id, order_id FROM report JOIN orders ON report.order_id = orders.id JOIN invoice " +
                "ON orders.invoice_id = invoice.id ORDER BY isConfirmed ASC, isPaid ASC, order_id DESC LIMIT ?, 10;";
        List<Report> reportList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, index);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                Order order = new Order();
                Client client = new Client();
                order.setId(resultSet.getLong("order_id"));
                client.setId(resultSet.getLong("client_id"));
                report.setOrder(order);
                report.setClient(client);
                reportList.add(report);
            }
        }
        return reportList;
    }

    @SneakyThrows
    public List<Report> getNotConfirmedOrders() {
        String sql = "SELECT client_id, order_id FROM report JOIN orders ON report.order_id = orders.id WHERE orders.isConfirmed = false ORDER BY order_id DESC";
        List<Report> reportList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long clientId = resultSet.getLong("client_id");
                Long orderId = resultSet.getLong("order_id");
                client.setId(clientId);
                order.setId(orderId);
                report.setClient(client);
                report.setOrder(order);
                reportList.add(report);
            }
        }
        return reportList;
    }

    @SneakyThrows
    public List<Report> getReportByDayAndDirection(String arrivalDate, String senderCity, String recipientCity) {
        List<Report> reportList = new ArrayList<>();
        String sql = "SELECT client_id, order_id FROM report JOIN orders ON report.order_id = orders.id JOIN delivery " +
                "ON orders.delivery_id = delivery.id JOIN route ON delivery.route_id = route.id WHERE arrival_date = ? AND sender_city = ? AND recipient_city = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(arrivalDate));
            preparedStatement.setString(2, senderCity);
            preparedStatement.setString(3, recipientCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long clientId = resultSet.getLong("client_id");
                Long orderId = resultSet.getLong("order_id");
                client.setId(clientId);
                order.setId(orderId);
                report.setClient(client);
                report.setOrder(order);
                reportList.add(report);
            }
        }
        return reportList;
    }

    @SneakyThrows
    public void confirmOrder(Long orderId) {
        String sql = "UPDATE orders SET isConfirmed = true WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.execute();
        }
    }
}
