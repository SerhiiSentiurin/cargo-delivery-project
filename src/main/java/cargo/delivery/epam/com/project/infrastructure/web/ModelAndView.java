package cargo.delivery.epam.com.project.infrastructure.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelAndView {
    private String view;
    private Map<String, Object> attributes = new HashMap<>();
    private boolean isRedirect;

    public static ModelAndView withView(String view) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.attributes = new HashMap<>();
//        modelAndView.isRedirect = false;
//        modelAndView.view = view;
//        return modelAndView;
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
