package com.itinfo.itcloud.configuration

import com.itinfo.common.LoggerDelegate
/*
import com.itinfo.security.CustomAccessDeniedHandler
import com.itinfo.security.CustomAuthFailureHandler
import com.itinfo.security.CustomAuthProvider
import com.itinfo.security.SecurityConnectionService
*/
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy

import org.springframework.security.web.SecurityFilterChain

import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig(

) {

	@Bean
	@Throws(Exception::class)
	fun filterChain(http: HttpSecurity): SecurityFilterChain? {
		log.debug("... filterChan")
		http.cors()
			.and()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			// .accessDeniedHandler(jwtAccessDeniedHandler)
			.and()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
/*
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/loginSuccess", true)
				.usernameParameter("userid")
				.passwordParameter("passwd")
				.successForwardUrl("/loginSuccess")
				// .successHandler(customAuthenticationProvider())
				.failureUrl("/login")
//				.failureHandler(customAuthFailureHandler())
				.permitAll()
			.and()
*/
        return http.build()
	}
/*
    @Bean
    fun customAuthenticationProvider(): CustomAuthProvider {
        log.info("... customAuthenticationProvider")
        return CustomAuthProvider()
    }

	@Bean
	fun customAccessDeniedHandler(): CustomAccessDeniedHandler {
		log.info("... customAccessDeniedHandler")
		return CustomAccessDeniedHandler()
	}

	@Bean
	fun customAuthFailureHandler(): CustomAuthFailureHandler {
		log.info("... customAuthFailureHandler")
		return CustomAuthFailureHandler()
	}

	@Bean
	fun securityConnectionService(): SecurityConnectionService {
		log.info("securityConnectionService ... ")
		return SecurityConnectionService()
	}
*/
	companion object {
		private val log by LoggerDelegate()
	}
}