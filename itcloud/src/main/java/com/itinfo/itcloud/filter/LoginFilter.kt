package com.itinfo.itcloud.filter

import com.itinfo.common.LoggerDelegate
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns=[
	"/api/v1/**",
])
class LoginFilter(
	// private val tokenProvider: TokenProvider,
	// private val refreshTokenRepository: RefreshTokenRepository,
): OncePerRequestFilter() {

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		TODO("NOT IMPLEMENTED")
		/*
		tokenProvider.resolveToken(request as? HttpServletRequest)?.also {
			val loginToken = it.split(" ")[1]
			val findToken = refreshTokenRepository.findByAccessToken(loginToken)

			if (findToken == null ||
				LocalDateTime.now().isAfter(findToken.accessTokenExpireAt) ||
				!it.toLowerCase()
					.startsWith("bearer ") || !tokenProvider.validateToken(loginToken)
			) {
				response.sendError(HttpStatus.UNAUTHORIZED.value())
				return
			}
			val authentication = tokenProvider.getAuthentication(loginToken)
			SecurityContextHolder.getContext().authentication = authentication
		}
		filterChain.doFilter(request, response)
		*/
	}
}