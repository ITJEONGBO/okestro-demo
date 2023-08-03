package com.itinfo.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itinfo.ItInfoConstant;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response,
										AuthenticationException authenticationException) throws IOException, ServletException {
		log.error("CustomAuthFailurehandler message : " + authenticationException.getMessage());
		if (authenticationException.getMessage().equalsIgnoreCase(ItInfoConstant.CONNECTION_TIME_OUT))
			response.setStatus(ItInfoConstant.STATUS_CONNECTION_TIME_OUT);
		else if (authenticationException.getMessage().equalsIgnoreCase(ItInfoConstant.READ_TIME_OUT))
			response.setStatus(ItInfoConstant.STATUS_READ_TIME_OUT);
		else if (authenticationException.getMessage().equalsIgnoreCase(ItInfoConstant.PASSWORD_ERROR))
			response.setStatus(ItInfoConstant.STATUS_PASSWORD_ERROR);
		else if (authenticationException.getMessage().equalsIgnoreCase(ItInfoConstant.ACCESS_DENIED_LOCKED))
			response.setStatus(ItInfoConstant.STATUS_ACCESS_DENIED_LOCKED);
		else if (authenticationException.getMessage().equalsIgnoreCase(ItInfoConstant.LOGIN_ATTEMPT_EXCEED))
			response.setStatus(ItInfoConstant.STATUS_LOGIN_ATTEMPT_EXCEED);
		else
			response.setStatus(ItInfoConstant.STATUS_ERROR);
		request.getRequestDispatcher("/login").forward(request, response);
	}
}

