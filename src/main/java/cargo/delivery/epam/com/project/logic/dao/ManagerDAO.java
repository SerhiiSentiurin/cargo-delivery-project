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
    public List<Report> getAllOrders() {
        String sql = "select client_id, order_id from report join orders on report.order_id = orders.id join invoice on orders.invoice_id = invoice.id order by isConfirmed asc, isPaid asc, order_id desc";
        List<Report> reportList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
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
        String sql = "select client_id, order_id from report join orders on report.order_id = orders.id where orders.isConfirmed = false order by order_id desc";
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
    public List<Report> getReportByDayAndDirection(String arrivalDate, String senderCity, String recipientCity){
        List<Report> reportList = new ArrayList<>();
        String sql = "select client_id, order_id from report join orders on report.order_id = orders.id join delivery on orders.delivery_id = delivery.id join route on delivery.route_id = route.id where arrival_date = ? and sender_city = ? and recipient_city = ?";
        try(Connection connection  = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setDate(1,Date.valueOf(arrivalDate));
            preparedStatement.setString(2, senderCity);
            preparedStatement.setString(3,recipientCity);
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
        String sql = "update orders set isConfirmed = true where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.execute();
        }
    }

}
