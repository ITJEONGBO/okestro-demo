package com.itinfo.security;

import com.itinfo.ItInfoConstant;
import com.itinfo.service.SystemPropertiesService;
import com.itinfo.service.UsersService;
import com.itinfo.model.SystemPropertiesVo;
import com.itinfo.model.UserVo;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Slf4j
public class CustomAuthProvider implements AuthenticationProvider {
	@Autowired private UsersService usersService;
	@Autowired private SystemPropertiesService systemPropertiesService;
	@Autowired private SecurityUtils securityUtils;
	@Autowired private SecurityConnectionService securityConnectionService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("... authenticate");
		String userId = authentication.getPrincipal().toString();
		String passwd = this.securityUtils.decodeBase64(authentication.getCredentials().toString());
		String retrievePasswd = usersService.login(userId);

		WebAuthenticationDetails wad = (WebAuthenticationDetails) authentication.getDetails();
		String userIPAddress = wad.getRemoteAddress();
		try {
			String blockTime = usersService.retrieveUser(userId).getBlockTime();
			if (!blockTime.isEmpty()) {
				if (!SecurityUtils.compareTime(blockTime))
					throw new BadCredentialsException("loginAttemptExceed");
				usersService.initLoginCount(userId);
			}

			if (SecurityUtils.validatePassword(passwd, retrievePasswd)) {
				SystemPropertiesVo systemProperties = this.systemPropertiesService.retrieveSystemProperties();
				this.securityConnectionService.setUser(systemProperties.getId());
				this.securityConnectionService.setPassword(systemProperties.getPassword());
				log.info("Login successful [ userId : {} ] [ request ip : {} ]", systemProperties.getId(), userIPAddress);
			} else {
				UserVo user = usersService.retrieveUser(userId);
				user.setLoginCount(user.getLoginCount() + 1);
				usersService.updateLoginCount(user);
				if (this.systemPropertiesService.retrieveSystemProperties().getLoginLimit() <= user.getLoginCount())
					usersService.setBlockTime(user);
				log.error("Login fail [ userId :  {} ] [ request ip : {} ]", userId, userIPAddress);
				throw new BadCredentialsException(ItInfoConstant.PASSWORD_ERROR);
			}
			usersService.initLoginCount(userId);
			List<GrantedAuthority> roles = new ArrayList<>();
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userId, passwd, roles);
			result.setDetails(new MemberService(userId, passwd, this.securityConnectionService.getUrl()));
			return result;
		} catch (Exception e) {
			String exception = ExceptionUtils.getStackTrace(e);
			String message = authenticationExceptionMessage(exception);
			if (!message.equalsIgnoreCase(ItInfoConstant.CONNECTION_TIME_OUT))
				message = e.getMessage();
			throw new BadCredentialsException(message);
		}
	}

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public String authenticationExceptionMessage(String exception) {
		return (exception.indexOf("org.apache.http.conn.HttpHostConnectException") > 0)
				? ItInfoConstant.CONNECTION_TIME_OUT
				: "";
	}
}
