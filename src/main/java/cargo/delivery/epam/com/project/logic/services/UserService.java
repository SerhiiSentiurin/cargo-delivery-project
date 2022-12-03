package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.UserDAO;
import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public User getUserByLogin(UserDto userDto) {
        log.info("Trying to enter user");

        User user = userDAO.getUserByLogin(userDto.getLogin())
                .orElseThrow(() -> new AppException("User with this login does not exist!"));
        if (!user.getPassword().equals(userDto.getPassword())) {
            throw new AppException("Password is incorrect!");
        }

        log.info("User was entered");
        return user;
    }
}
