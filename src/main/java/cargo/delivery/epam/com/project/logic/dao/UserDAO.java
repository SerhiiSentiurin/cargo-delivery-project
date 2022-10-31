package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Manager;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public Optional<User> getUserByLogin(String login) {
        String sql = "SELECT * FROM user WHERE login = ?";
        User enteringUser;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String userRole = resultSet.getString("role");
                if (userRole.equals("MANAGER")) {
                    enteringUser = new Manager();
                    enteringUser.setUserRole(UserRole.MANAGER);
                } else {
                    enteringUser = new Client();
                    enteringUser.setUserRole(UserRole.CLIENT);
                }
                enteringUser.setId(id);
                enteringUser.setLogin(login);
                enteringUser.setPassword(password);
                return Optional.of(enteringUser);
            }
        }
        return Optional.empty();
    }
}
