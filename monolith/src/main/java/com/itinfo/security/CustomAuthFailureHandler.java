package com.itinfo.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
		log.error("CustomAuthenticationfailurehandler message : " + authenticationException.getMessage());
		if (authenticationException.getMessage().equalsIgnoreCase("connectionTimeOut"))
			response.setStatus(300);
		else if (authenticationException.getMessage().equalsIgnoreCase("readTimedOut"))
			response.setStatus(301);
		else if (authenticationException.getMessage().equalsIgnoreCase("passwordError"))
			response.setStatus(302);
		else if (authenticationException.getMessage().equalsIgnoreCase("accessDeniedLocked"))
			response.setStatus(303);
		else if (authenticationException.getMessage().equalsIgnoreCase("loginAttemptExceed"))
			response.setStatus(429);
		else
			response.setStatus(500);
		request.getRequestDispatcher("/login").forward((ServletRequest)request, (ServletResponse)response);
	}
}

