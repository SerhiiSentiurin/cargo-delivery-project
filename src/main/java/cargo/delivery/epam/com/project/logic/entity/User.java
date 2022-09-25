package cargo.delivery.epam.com.project.logic.entity;

import lombok.Data;

@Data
public abstract class User {
    private Long id;
    private String login;
    private String password;
    private UserRole userRole;
}
