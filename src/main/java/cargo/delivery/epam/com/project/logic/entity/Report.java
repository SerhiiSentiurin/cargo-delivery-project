package cargo.delivery.epam.com.project.logic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
//    private Long id;
    private Client client;
    private Order order;

}
