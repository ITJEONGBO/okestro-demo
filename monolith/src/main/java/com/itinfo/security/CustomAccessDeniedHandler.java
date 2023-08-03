package com.itinfo.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Setter
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	private String errorPage;
	public void handle(HttpServletRequest request,
					   HttpServletResponse response,
					   AccessDeniedException e) throws ServletException, IOException {
		log.info("..." + this.errorPage);
		log.info("Exception : {}", e.getClass().getCanonicalName());
		log.info("LocalizedMessage : {}", e.getLocalizedMessage());
		log.info("Message : {}", e.getMessage());
		log.info("StackTrace : {}", e.getStackTrace());

		request.setAttribute("errMsg", e.getMessage());
		request.getRequestDispatcher("/WEB-INF/jsp/com/itinfo/login/accessDenied.jsp").forward(request, response);
	}
}

