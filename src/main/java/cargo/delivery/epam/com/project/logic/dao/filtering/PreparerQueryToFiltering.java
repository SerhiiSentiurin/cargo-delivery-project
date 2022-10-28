package cargo.delivery.epam.com.project.logic.dao.filtering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;

public class PreparerQueryToFiltering {
    public String buildCheckedQueryToFiltering(FilteringDto dto) {
        String startQuery = "select client.id, orders.id ";
        String middleQuery = checkFullQuery(dto);
        String endQuery = " order by isConfirmed asc, isPaid asc, order_id desc limit ?, 10";
        return startQuery.concat(middleQuery).concat(endQuery);
    }

    public String buildCheckedQueryToCountRows(FilteringDto dto) {
        String startQuery = "select count(*) ";
        String endQuery = checkFullQuery(dto);
        return startQuery.concat(endQuery);
    }

    private String checkFullQuery(FilteringDto dto) {
        String departureDateInQuery = checkDepartureDateDtoField(dto);
        String arrivalDateInQuery = checkArrivalDateDtoField(dto);
        String isConformedInQuery = checkIsConfirmedDtoField(dto);
        String isPaidInQuery = checkIsPaidDtoField(dto);
        return "from report join client on report.client_id=client.id join user on user.id=client.id join orders on report.order_id=orders.id " +
                "join delivery on orders.delivery_id=delivery.id join invoice on orders.invoice_id=invoice.id join route on delivery.route_id=route.id " +
                "where orders.id like ? and user.login like ? and orders.type like ? and orders.weight like ? and orders.volume like ? and route.sender_city like ? " +
                "and route.recipient_city like ? and route.distance like ? and " + departureDateInQuery + " and " + arrivalDateInQuery + " and invoice.price like ? " +
                "and orders.isConfirmed " + isConformedInQuery + " and invoice.isPaid " + isPaidInQuery;
    }

    private String checkDepartureDateDtoField(FilteringDto dto) {
        String departureDateInQuery;
        if (dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "(delivery.departure_date like ? or delivery.departure_date is null)";
        } else {
            departureDateInQuery = "delivery.departure_date = ?";
        }
        return departureDateInQuery;
    }

    private String checkArrivalDateDtoField(FilteringDto dto) {
        String arrivalDateInQuery;
        if (dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "(delivery.arrival_date like ? or delivery.arrival_date is null)";
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
            return "like ?";
        } else {
            return "= ?";
        }
    }
}
