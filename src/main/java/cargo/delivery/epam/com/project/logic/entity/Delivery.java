package cargo.delivery.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    private Long id;
    private String departureDate;
    private String arrivingDate;
    private Route route;
}
