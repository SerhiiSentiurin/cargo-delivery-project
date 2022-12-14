package cargo.delivery.epam.com.project.infrastructure.web.listener;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import cargo.delivery.epam.com.project.infrastructure.config.servlet.FrontServletInitializer;

import java.util.List;
import java.util.Locale;

/**
 * This is implementation of HttpSessionListener, and gets notified of changes to the list of active sessions in a web application.
 * Configured in FrontServletInitializer. Used for changing local of application web interface.
 *
 * @see HttpSessionListener
 * @see FrontServletInitializer
 */
@Log4j2
@RequiredArgsConstructor
public class SessionListenerLocales implements HttpSessionListener {
    private final List<Locale> locales;
    private final Locale selectedLocale;

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        session.setAttribute("locales", locales);
        session.setAttribute("selectedLocale", selectedLocale);
        log.info("Session created. Locale - " + selectedLocale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        sessionEvent.getSession().invalidate();
        log.info("Session destroyed.");
    }
}
