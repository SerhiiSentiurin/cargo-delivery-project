package cargo.delivery.epam.com.project.logic.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String senderCity;
    private String recipientCity;
    private Double distance;
    private Double deliveryCost;
}
