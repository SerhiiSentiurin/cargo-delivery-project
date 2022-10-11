package cargo.delivery.epam.com.project.logic.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingDto {
    private Long orderId;
    private String login;
    private String type;
    private Double weight;
    private Double volume;
    private String senderCity;
    private String recipientCity;
    private Double distance;
    private String departureDate;
    private String arrivalDate;
    private Double price;
    private Boolean isConfirmed;
    private Boolean isPaid;
}
