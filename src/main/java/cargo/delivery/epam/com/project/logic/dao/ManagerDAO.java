package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.*;
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
public class ManagerDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public List<Report> getAllOrders() {
        String sql = "select * from report order by order_id desc";
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
        String sql = "select client.id,orders.id from report join client on report.client_id=client.id join orders on report.order_id=orders.id join delivery on orders.delivery_id=delivery.id join invoice on orders.invoice_id=invoice.id join route on delivery.route_id=route.id where orders.isConfirmed = false";
        List<Report> reportList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long clientId = resultSet.getLong("client.id");
                Long orderId = resultSet.getLong("orders.id");
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
