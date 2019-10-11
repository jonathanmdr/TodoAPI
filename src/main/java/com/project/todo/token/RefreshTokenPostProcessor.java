package com.project.todo.token;

import static com.project.todo.token.utils.RefreshTokenProcessorUtils.COOKIE_NAME;
import static com.project.todo.token.utils.RefreshTokenProcessorUtils.NAME_METHOD_PARAMETER;
import static com.project.todo.token.utils.RefreshTokenProcessorUtils.URI_TOKEN;

import com.project.todo.config.property.TodoApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Profile("oauth-security")
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    @Autowired
    private TodoApiProperty todoApiProperty;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterClass) {
        return returnType.getMethod().getName().equals(NAME_METHOD_PARAMETER);
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken, MethodParameter methodParameter,
                                             MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                             ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        addRefreshTokenCookie(refreshToken, req, res);
        removeRefreshTokenBody(token);

        return oAuth2AccessToken;
    }

    private void removeRefreshTokenBody(DefaultOAuth2AccessToken token) {
        token.setRefreshToken(null);
    }

    private void addRefreshTokenCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = new Cookie(COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(todoApiProperty.getSecurity().isEnableHttps());
        refreshTokenCookie.setPath(req.getContextPath().concat(URI_TOKEN));
        refreshTokenCookie.setMaxAge(2592000);
        res.addCookie(refreshTokenCookie);
    }

}
