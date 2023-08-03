package com.itinfo.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itinfo.ItInfoConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

@Slf4j
public class UserConvenienceInterceptor extends HandlerInterceptorAdapter {
	private String paramName;
	private Cookie userConvenienceCookie;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String menuSize = request.getParameter(getParamName());
		if (WebUtils.getCookie(request, "userConvenienceCookie") != null) {
			this.userConvenienceCookie = WebUtils.getCookie(request, "userConvenienceCookie");
			if (menuSize != null)
				if (menuSize.equalsIgnoreCase("M")) {
					this.userConvenienceCookie.setValue(
							menuSize.equalsIgnoreCase("M")
									? ItInfoConstant.MENU_SIZE_M
									: ItInfoConstant.MENU_SIZE_S
					);
				}
		} else if (menuSize != null) {
			if (menuSize.equalsIgnoreCase("M")) {
				this.userConvenienceCookie = new Cookie("userConvenienceCookie", ItInfoConstant.MENU_SIZE_M);
				log.info("쿠키 설정함 M");
			} else {
				this.userConvenienceCookie = new Cookie("userConvenienceCookie", ItInfoConstant.MENU_SIZE_S);
			}
		} else {
			this.userConvenienceCookie = new Cookie("userConvenienceCookie", ItInfoConstant.MENU_SIZE_M);
		}
		this.userConvenienceCookie.setMaxAge(63072000);
		this.userConvenienceCookie.setPath("/");
		response.addCookie(this.userConvenienceCookie);
		return true;
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
