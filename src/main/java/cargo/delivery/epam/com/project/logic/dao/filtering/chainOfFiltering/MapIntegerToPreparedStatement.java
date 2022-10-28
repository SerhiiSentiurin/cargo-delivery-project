package cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public class MapIntegerToPreparedStatement implements MapDtoFieldToPreparedStatement{
    @SneakyThrows
    @Override
    public void map(Object field, PreparedStatement preparedStatement, int index, FilteringDto dto) {
        Integer integerValue = (Integer) field;
        preparedStatement.setInt(index, integerValue);
    }

    @Override
    public boolean canMap(Object field) {
        return field.getClass().equals(Integer.class);
    }
}
