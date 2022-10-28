package cargo.delivery.epam.com.project.logic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
    private Double amount;

    public Client() {
        this.setUserRole(UserRole.CLIENT);
    }

}
