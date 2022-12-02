package cargo.delivery.epam.com.project.logic.dao.filtering;

import cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering.MapDtoFieldToPreparedStatement;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class uses for setting field into PreparedStatement depending on their condition.
 * This filters records and retrieves data from the database based on certain
 * criteria (not empty or not null fields in the DTO object).
 * If field of DTO object is null or empty - filtering by this field is not performed.
 * If field of DTO object is not null or not empty - filtering is performed with the participation of this field.
 */
@RequiredArgsConstructor
public class SetterFilteredFieldToPreparedStatement {
    private final List<MapDtoFieldToPreparedStatement> fieldMappers;

    /**
     * Collects field from DTO object to list.
     *
     * @param dto data-transfer-object with filtering criteria (fields)
     * @return list with DTO object fields
     */
    private List<Object> collectToListDtoFields(FilteringDto dto) {
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
        if (dto.getPage() != null) {
            dtoFields.add(dto.getPage());
        }
        return dtoFields;
    }

    /**
     * Sets fields from DTO object to PreparedStatement. "Chain of responsibility pattern" applied here.
     *
     * @param preparedStatement created above instance of PreparedStatement
     * @param dto data-transfer-object with filtering criteria (fields)
     * @see MapDtoFieldToPreparedStatement
     */
    @SneakyThrows
    public void setFieldsFromDtoToPreparedStatement(PreparedStatement preparedStatement, FilteringDto dto) {
        List<Object> dtoFields = collectToListDtoFields(dto);
        for (int i = 0; i < dtoFields.size(); i++) {
            int index = i + 1;
            Object field = dtoFields.get(i);
            fieldMappers.stream()
                    .filter(mapper -> mapper.checkNullField(field, preparedStatement, index))
                    .filter(mapper -> mapper.canMap(field))
                    .findFirst()
                    .ifPresent(mapper -> mapper.map(field, preparedStatement, index, dto));
        }
    }


    /**
     * @param preparedStatement created above instance of PreparedStatement
     * @param dto data-transfer-object with filtering criteria (fields)
     * @deprecated because method isn't readable. The solution is not optimal.
     */
    @Deprecated
    @SneakyThrows
    public void setFieldsFromDtoToPrStmt(PreparedStatement preparedStatement, FilteringDto dto) {
        List<Object> dtoFields = collectToListDtoFields(dto);
        int index = 0;
        for (Object field : dtoFields) {
            if (field == null || field.toString().isEmpty()) {
                preparedStatement.setString(++index, "%%");
            } else if (field.equals(dto.getDepartureDate()) || field.equals(dto.getArrivalDate())) {
                preparedStatement.setDate(++index, Date.valueOf(field.toString()));
            } else if (field.equals(dto.getOrderId())) {
                preparedStatement.setLong(++index, (Long) field);
            } else if (field.equals(dto.getPage())) {
                preparedStatement.setInt(++index, (Integer) field);
            } else if (field instanceof Boolean) {
                preparedStatement.setBoolean(++index, (Boolean) field);
            } else {
                preparedStatement.setString(++index, "%" + field + "%");
            }
        }
    }
}
