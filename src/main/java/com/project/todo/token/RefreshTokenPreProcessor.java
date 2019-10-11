package com.project.todo.token;

import static com.project.todo.token.utils.RefreshTokenProcessorUtils.COOKIE_NAME;
import static com.project.todo.token.utils.RefreshTokenProcessorUtils.TOKEN_KEY;
import static com.project.todo.token.utils.RefreshTokenProcessorUtils.TOKEN_VALUE;
import static com.project.todo.token.utils.RefreshTokenProcessorUtils.URI_TOKEN;

import org.apache.catalina.util.ParameterMap;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

@Profile("oauth-security")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessor implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (URI_TOKEN.equalsIgnoreCase(req.getRequestURI())
                && TOKEN_VALUE.equals(req.getParameter(TOKEN_KEY))
                && req.getCookies() != null) {

            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    String refreshToken = cookie.getValue();
                    req = new MyServletRequestWrapper(req, refreshToken);
                }
            }
        }

        chain.doFilter(req, response);
    }

    static class MyServletRequestWrapper extends HttpServletRequestWrapper {

        private String refreshToken;

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put(TOKEN_VALUE, new String[]{refreshToken});
            map.setLocked(true);
            return map;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
