package cargo.delivery.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    private Long id;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private Route route;
}
