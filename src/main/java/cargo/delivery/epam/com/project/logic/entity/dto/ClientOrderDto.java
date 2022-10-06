package cargo.delivery.epam.com.project.logic.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrderDto {
    private Long clientId;
    private String type;
    private Double weight;
    private Double volume;
    private String senderCity;
    private String recipientCity;
    private Double distance;
    private Double deliveryCost;

}
