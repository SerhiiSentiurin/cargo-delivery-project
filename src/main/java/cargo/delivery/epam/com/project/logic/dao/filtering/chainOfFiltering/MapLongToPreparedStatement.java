package cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public class MapLongToPreparedStatement implements MapDtoFieldToPreparedStatement{
    @SneakyThrows
    @Override
    public void map(Object field, PreparedStatement preparedStatement, int index, FilteringDto dto) {
        Long longValue = (Long) field;
            preparedStatement.setLong(index,longValue);
    }

    @Override
    public boolean canMap(Object field) {
        return field.getClass().equals(Long.class);
    }
}
