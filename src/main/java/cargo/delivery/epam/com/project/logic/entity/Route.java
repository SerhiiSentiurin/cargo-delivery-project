package cargo.delivery.epam.com.project.logic.entity;

import liquibase.pro.packaged.A;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private Long id;
    private Double distance;
    private String senderCity;
    private String recipientCity;
}
