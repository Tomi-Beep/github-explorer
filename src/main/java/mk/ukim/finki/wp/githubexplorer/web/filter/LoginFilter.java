package mk.ukim.finki.wp.githubexplorer.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (needsLogin(httpRequest) && !isLoggedIn(httpRequest)) {
            String redirect = httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                redirect += "?" + httpRequest.getQueryString();
            }

            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/login?redirect="
                    + URLEncoder.encode(redirect, StandardCharsets.UTF_8));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean needsLogin(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/watchlist") || path.startsWith("/repositories/save");
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("currentUserId") != null;
    }
}