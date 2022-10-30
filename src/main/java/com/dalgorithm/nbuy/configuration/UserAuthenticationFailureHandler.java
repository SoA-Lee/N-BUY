package com.dalgorithm.nbuy.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";

        if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = exception.getMessage();
        }

        setUseForward(true);
        setDefaultFailureUrl("/members/login?error=true");
        request.setAttribute("errorMessage", errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
