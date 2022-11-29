package cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering;

import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

public interface MapDtoFieldToPreparedStatement {
    @SneakyThrows
    default boolean checkNullField(Object field, PreparedStatement preparedStatement, int index) {
        if (field != null) {
            return true;
        }
        preparedStatement.setString(index, "%%");
        return false;
    }

    void map(Object field, PreparedStatement preparedStatement, int index, FilteringDto dto);

    boolean canMap(Object field);

}
