package cargo.delivery.epam.com.project.logic.entity.dto;

import cargo.delivery.epam.com.project.logic.entity.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ClientCreateDto {
    private Long id;
    private String login;
    private String password;
    private UserRole userRole;

    public ClientCreateDto(){
        this.userRole = UserRole.CLIENT;
    }

    public ClientCreateDto(Long id, String login,String password){
        this.id = id;
        this.login = login;
        this.password = password;
        this.userRole = UserRole.CLIENT;
    }

}
