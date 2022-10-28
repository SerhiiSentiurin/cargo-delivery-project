package cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public class MapDoubleToPreparedStatement implements MapDtoFieldToPreparedStatement {
    @SneakyThrows
    @Override
    public void map(Object field, PreparedStatement preparedStatement, int index, FilteringDto dto) {
        Double doubleValue = (Double) field;
        preparedStatement.setString(index, "%" + doubleValue + "%");
    }

    @Override
    public boolean canMap(Object field) {
        return field.getClass().equals(Double.class);
    }
}
