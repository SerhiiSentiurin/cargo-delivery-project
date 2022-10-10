package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ReportDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public List<Report> getAllOrders() {
        String sql = "select * from report";
        List<Report> reportList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
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

}
