package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SortingDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public List<Report> select(SortingDto dto) {

        List<Report> reportList = new ArrayList<>();
        String departureDateInQuery = checkDtoDepartureDate(dto);
        String arrivalDateInQuery = checkDtoArrivalDate(dto);
        String isConformedInQuery = checkDtoIsConfirmed(dto);
        String isPaidInQuery = checkDtoIsPaid(dto);

        String sql = "select client.id, orders.id from report join client on report.client_id=client.id join user on user.id=client.id join orders on report.order_id=orders.id " +
                "join delivery on orders.delivery_id=delivery.id join invoice on orders.invoice_id=invoice.id join route on delivery.route_id=route.id " +
                "where orders.id like ? and user.login like ? and orders.type like ? and orders.weight like ? and orders.volume like ? and route.sender_city like ? " +
                "and route.recipient_city like ? and route.distance like ? and " + departureDateInQuery + " and " + arrivalDateInQuery + " and invoice.price like ? " +
                "and orders.isConfirmed " + isConformedInQuery + " and invoice.isPaid " + isPaidInQuery + " order by orders.id desc";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            checkSortingDtoToNull(preparedStatement, dto);
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

    private String checkDtoIsPaid(SortingDto dto) {
        String isPaidInQuery = null;

        if (dto.getIsPaid() == null) {
            isPaidInQuery = "like '%%'";
        }
        if (dto.getIsPaid() != null) {
            isPaidInQuery = "= ?";
        }
        return isPaidInQuery;
    }

    private String checkDtoIsConfirmed(SortingDto dto) {
        String isConformedInQuery = null;

        if (dto.getIsConfirmed() == null) {
            isConformedInQuery = "like '%%'";
        }
        if (dto.getIsConfirmed() != null) {
            isConformedInQuery = "= ?";
        }
        return isConformedInQuery;
    }

    private String checkDtoArrivalDate(SortingDto dto) {
        String arrivalDateInQuery = null;
        if (dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "(delivery.arrival_date like '%%' or delivery.arrival_date is null)";
        }
        if (!dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "delivery.arrival_date = ?";
        }
        return arrivalDateInQuery;
    }

    private String checkDtoDepartureDate(SortingDto dto) {
        String departureDateInQuery = null;
        if (dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "(delivery.departure_date like '%%' or delivery.departure_date is null)";
        }
        if (!dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "delivery.departure_date = ?";
        }
        return departureDateInQuery;
    }

    @SneakyThrows
    private void checkSortingDtoToNull(PreparedStatement preparedStatement, SortingDto dto) {
        int index = 0;
        if (dto.getOrderId() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getOrderId() != null) {
            preparedStatement.setString(++index, dto.getOrderId().toString());
        }


        if (dto.getLogin() == null || dto.getLogin().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }else {
            preparedStatement.setString(++index, "%" + dto.getLogin() + "%");
        }


        if (dto.getType().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getType().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getType() + "%");
        }
        if (dto.getWeight() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getWeight() != null) {
            preparedStatement.setString(++index, "%" + dto.getWeight() + "%");
        }
        if (dto.getVolume() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getVolume() != null) {
            preparedStatement.setString(++index, "%" + dto.getVolume().toString() + "%");
        }
        if (dto.getSenderCity().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getSenderCity().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getSenderCity() + "%");
        }
        if (dto.getRecipientCity().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getRecipientCity().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getRecipientCity() + "%");
        }
        if (dto.getDistance() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getDistance() != null) {
            preparedStatement.setString(++index, "%" + dto.getDistance().toString() + "%");
        }
        if (!dto.getDepartureDate().isEmpty()) {
            preparedStatement.setDate(++index, Date.valueOf(dto.getDepartureDate()));
        }
        if (!dto.getArrivalDate().isEmpty()) {
            preparedStatement.setDate(++index, Date.valueOf(dto.getArrivalDate()));
        }
        if (dto.getPrice() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getPrice() != null) {
            preparedStatement.setString(++index, "%" + dto.getPrice().toString() + "%");
        }
        if (dto.getIsConfirmed() != null) {
            preparedStatement.setBoolean(++index, dto.getIsConfirmed());
        }
        if (dto.getIsPaid() != null) {
            preparedStatement.setBoolean(++index, dto.getIsPaid());
        }
    }

}
