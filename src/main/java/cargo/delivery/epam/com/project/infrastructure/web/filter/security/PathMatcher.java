package cargo.delivery.epam.com.project.infrastructure.web.filter.security;

import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class PathMatcher {

    String path;
    List<UserRole> rolesToAccess;

    public PathMatcher(String path, UserRole... roles) {
        this.path = path;
        this.rolesToAccess = Arrays.asList(roles);
    }

    public boolean isMatchPath(String path) {
        return path.matches(this.path);
    }

    public boolean hasRole(User user) {
        return user != null && rolesToAccess.contains(user.getUserRole());
    }

}
