package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class PreparerQueryToReportFilteringDao {

    public String buildCheckedQueryToFiltering(SortingDto dto){
        String departureDateInQuery = checkDepartureDateField(dto);
        String arrivalDateInQuery = checkArrivalDateField(dto);
        String isConformedInQuery = checkIsConfirmedField(dto);
        String isPaidInQuery = checkIsPaidField(dto);

        return "select client.id, orders.id from report join client on report.client_id=client.id join user on user.id=client.id join orders on report.order_id=orders.id " +
                "join delivery on orders.delivery_id=delivery.id join invoice on orders.invoice_id=invoice.id join route on delivery.route_id=route.id " +
                "where orders.id like ? and user.login like ? and orders.type like ? and orders.weight like ? and orders.volume like ? and route.sender_city like ? " +
                "and route.recipient_city like ? and route.distance like ? and " + departureDateInQuery + " and " + arrivalDateInQuery + " and invoice.price like ? " +
                "and orders.isConfirmed " + isConformedInQuery + " and invoice.isPaid " + isPaidInQuery + " order by isConfirmed asc, isPaid asc, order_id desc";
    }

    @SneakyThrows
    public void checkSortingDtoToNull(PreparedStatement preparedStatement, SortingDto dto) {
        List<Object> dtoFields = collectToListDtoFields(dto);
        int index = 0;
        for (Object field : dtoFields) {
            if (field == null || field.toString().isEmpty()) {
                preparedStatement.setString(++index, "%%");
            } else if (field.equals(dto.getDepartureDate()) || field.equals(dto.getArrivalDate())) {
                preparedStatement.setDate(++index, Date.valueOf(field.toString()));
            } else if (field instanceof Long) {
                preparedStatement.setLong(++index, (Long) field);
            } else if (field instanceof Boolean) {
                preparedStatement.setBoolean(++index, (Boolean) field);
            } else {
                preparedStatement.setString(++index, "%" + field + "%");
            }
        }
    }

    private List<Object> collectToListDtoFields(SortingDto dto){
        List<Object> dtoFields = new ArrayList<>();
        dtoFields.add(dto.getOrderId());
        dtoFields.add(dto.getLogin());
        dtoFields.add(dto.getType());
        dtoFields.add(dto.getWeight());
        dtoFields.add(dto.getVolume());
        dtoFields.add(dto.getSenderCity());
        dtoFields.add(dto.getRecipientCity());
        dtoFields.add(dto.getDistance());
        dtoFields.add(dto.getDepartureDate());
        dtoFields.add(dto.getArrivalDate());
        dtoFields.add(dto.getPrice());
        dtoFields.add(dto.getIsConfirmed());
        dtoFields.add(dto.getIsPaid());
        return dtoFields;
    }

    private String checkDepartureDateField(SortingDto dto) {
        String departureDateInQuery;
        if (dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "(delivery.departure_date like ? or delivery.departure_date is null)";
        } else {
            departureDateInQuery = "delivery.departure_date = ?";
        }
        return departureDateInQuery;
    }

    private String checkArrivalDateField(SortingDto dto) {
        String arrivalDateInQuery;
        if (dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "(delivery.arrival_date like ? or delivery.arrival_date is null)";
        } else {
            arrivalDateInQuery = "delivery.arrival_date = ?";
        }
        return arrivalDateInQuery;
    }

    private String checkIsConfirmedField(SortingDto dto) {
        return fieldIsNull(dto.getIsConfirmed());
    }

    private String checkIsPaidField(SortingDto dto) {
        return fieldIsNull(dto.getIsPaid());
    }

    private String fieldIsNull(Object field) {
        if (field == null) {
            return "like ?";
        } else {
            return "= ?";
        }
    }


}
