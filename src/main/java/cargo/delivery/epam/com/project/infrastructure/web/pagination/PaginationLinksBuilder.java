package cargo.delivery.epam.com.project.infrastructure.web.pagination;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaginationLinksBuilder {

    private static final String REGEX = "page=\\d+";

    public List<String> buildLinks(HttpServletRequest request, int countOfPages) {
        List<String> links = new ArrayList<>();
        String url = request.getRequestURI() + "?" + Optional.ofNullable(request.getQueryString()).orElse("page=1");
        for (int i = 1; i <= countOfPages; i++) {
            links.add(url.replaceFirst(REGEX, "page=" + i));
        }
        return links;
    }
}
