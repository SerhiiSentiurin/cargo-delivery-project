package cargo.delivery.epam.com.project.logic.dao.filtering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;

public class PreparerQueryToFiltering {
    public String buildCheckedQueryToFiltering(FilteringDto dto) {
        String startQuery = "SELECT client.id, orders.id ";
        String middleQuery = checkFullQuery(dto);
        String endQuery = " ORDER BY isConfirmed ASC, isPaid ASC, order_id DESC limit ?, 10";
        return startQuery.concat(middleQuery).concat(endQuery);
    }

    public String buildCheckedQueryToCountRows(FilteringDto dto) {
        String startQuery = "SELECT count(*) ";
        String endQuery = checkFullQuery(dto);
        return startQuery.concat(endQuery);
    }

    private String checkFullQuery(FilteringDto dto) {
        String departureDateInQuery = checkDepartureDateDtoField(dto);
        String arrivalDateInQuery = checkArrivalDateDtoField(dto);
        String isConformedInQuery = checkIsConfirmedDtoField(dto);
        String isPaidInQuery = checkIsPaidDtoField(dto);
        return "FROM report JOIN client ON report.client_id=client.id JOIN user ON user.id=client.id JOIN orders ON report.order_id=orders.id " +
                "JOIN delivery ON orders.delivery_id=delivery.id JOIN invoice ON orders.invoice_id=invoice.id JOIN route ON delivery.route_id=route.id " +
                "WHERE orders.id LIKE ? AND user.login LIKE ? AND orders.type LIKE ? AND orders.weight LIKE ? AND orders.volume LIKE ? AND route.sender_city LIKE ? " +
                "AND route.recipient_city LIKE ? AND route.distance LIKE ? AND " + departureDateInQuery + " AND " + arrivalDateInQuery + " AND invoice.price LIKE ? " +
                "AND orders.isConfirmed " + isConformedInQuery + " AND invoice.isPaid " + isPaidInQuery;
    }

    private String checkDepartureDateDtoField(FilteringDto dto) {
        String departureDateInQuery;
        if (dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "(delivery.departure_date LIKE ? OR delivery.departure_date IS NULL)";
        } else {
            departureDateInQuery = "delivery.departure_date = ?";
        }
        return departureDateInQuery;
    }

    private String checkArrivalDateDtoField(FilteringDto dto) {
        String arrivalDateInQuery;
        if (dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "(delivery.arrival_date LIKE ? OR delivery.arrival_date IS NULL)";
        } else {
            arrivalDateInQuery = "delivery.arrival_date = ?";
        }
        return arrivalDateInQuery;
    }

    private String checkIsConfirmedDtoField(FilteringDto dto) {
        return fieldIsNull(dto.getIsConfirmed());
    }

    private String checkIsPaidDtoField(FilteringDto dto) {
        return fieldIsNull(dto.getIsPaid());
    }

    private String fieldIsNull(Object field) {
        if (field == null) {
            return "LIKE ?";
        } else {
            return "= ?";
        }
    }
}
