package cargo.delivery.epam.com.project.infrastructure.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of MVC pattern realization. Containing view (path to the view (JSP page)),
 * model (hashMap with attributes), boolean field to set forward or redirect method in ProcessorModelAndView.
 * Contain static factory method which return instance of this class with empty attributes and with view is came into.
 *
 * @see ProcessorModelAndView
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelAndView {
    private String view;
    private Map<String, Object> attributes = new HashMap<>();
    private boolean isRedirect;

    public static ModelAndView withView(String view) {
        return ModelAndView.builder()
                .attributes(new HashMap<>())
                .isRedirect(false)
                .view(view)
                .build();
    }

    public void addAttribute(String name, Object object) {
        attributes.put(name, object);
    }
}
