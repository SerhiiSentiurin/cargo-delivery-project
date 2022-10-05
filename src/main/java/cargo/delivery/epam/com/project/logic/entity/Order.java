package cargo.delivery.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String type;
    private Double weight;
    private Double volume;
    private Delivery delivery;
    private boolean isConfirmed;
}
