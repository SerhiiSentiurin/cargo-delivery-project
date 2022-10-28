package cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.sql.Date;
import java.sql.PreparedStatement;

@Log4j2
public class MapStringToPreparedStatement implements MapDtoFieldToPreparedStatement {
    @Override
    @SneakyThrows
    public void map(Object field, PreparedStatement preparedStatement, int index, FilteringDto dto) {
        String stringValue = (String) field;
        if (stringValue.isEmpty()) {
            preparedStatement.setString(index, "%%");
        } else if (stringValue.equals(dto.getDepartureDate()) || stringValue.equals(dto.getArrivalDate())) {
            preparedStatement.setDate(index, Date.valueOf(stringValue));
        } else {
            preparedStatement.setString(index, "%" + stringValue + "%");
        }

    }

    @Override
    public boolean canMap(Object field) {
        return field.getClass().equals(String.class);
    }

}
