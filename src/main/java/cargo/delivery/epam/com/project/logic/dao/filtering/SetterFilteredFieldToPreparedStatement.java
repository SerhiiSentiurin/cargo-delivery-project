package cargo.delivery.epam.com.project.logic.dao.filtering;

import cargo.delivery.epam.com.project.logic.dao.filtering.chainOfFiltering.MapDtoFieldToPreparedStatement;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SetterFilteredFieldToPreparedStatement {
    private final List<MapDtoFieldToPreparedStatement> fieldMappers;

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




//    @SneakyThrows
//    public void checkSortingDtoToNull(PreparedStatement preparedStatement, FilteringDto dto) {
//        List<Object> dtoFields = collectToListDtoFields(dto);
//        int index = 0;
//        for (Object field : dtoFields) {
//            if (field == null || field.toString().isEmpty()) {
//                preparedStatement.setString(++index, "%%");
//            } else if (field.equals(dto.getDepartureDate()) || field.equals(dto.getArrivalDate())) {
//                preparedStatement.setDate(++index, Date.valueOf(field.toString()));
//            } else if (field.equals(dto.getOrderId())) {
//                preparedStatement.setLong(++index, (Long) field);
//            } else if (field.equals(dto.getPage())) {
//                preparedStatement.setInt(++index, (Integer) field);
//            } else if (field instanceof Boolean) {
//                preparedStatement.setBoolean(++index, (Boolean) field);
//            } else {
//                preparedStatement.setString(++index, "%" + field + "%");
//            }
//        }
//    }
}
