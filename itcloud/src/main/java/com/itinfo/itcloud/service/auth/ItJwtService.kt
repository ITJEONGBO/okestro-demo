package com.itinfo.itcloud.service.auth

import com.itinfo.itcloud.service.BaseService
import com.itinfo.itcloud.service.admin.ItSystemPropertiesService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails

import java.util.*
import java.security.Key
import kotlin.collections.HashMap


interface ItJwtService {
}

class JwtServiceImpl(

): BaseService(), ItJwtService {
/*
	fun extractUsername(token: String?): String {
		return extractClaim(token, Claims::class)
	}

	fun <T> extractClaim(token: String?, claimsResolver: Claims.() -> T): T {
		val claims: Claims = extractAllClaims(token)
		return claimsResolver.apply(claims)
	}
*/
	fun generateToken(userDetails: UserDetails): String {
		return generateToken(hashMapOf(), userDetails)
	}

	fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
		return buildToken(extraClaims, userDetails, systemPropertiesVo.jwtExpirationTime)
	}

	fun getExpirationTime(): Long =
		systemPropertiesVo.jwtExpirationTime


	private fun buildToken(
		extraClaims: Map<String, Any>,
		userDetails: UserDetails,
		expiration: Long
	): String {
		return Jwts
			.builder()
			.setClaims(extraClaims)
			.setSubject(userDetails.username)
			.setIssuedAt(Date(System.currentTimeMillis()))
			.setExpiration(Date(System.currentTimeMillis() + expiration))
			.signWith(getSignInKey(), SignatureAlgorithm.HS256)
			.compact()
	}
	/*
	fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
		val username = extractUsername(token)
		return (username == userDetails.username) && !isTokenExpired(token)
	}

		private fun isTokenExpired(token: String?): Boolean {
			return extractExpiration(token).before(Date())
		}

		private fun extractExpiration(token: String?): Date =
			extractClaim(token, { obj: Claims -> obj.expiration })
	*/
	private fun extractAllClaims(token: String): Claims {
		return Jwts
			.parserBuilder()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
	}

	private fun getSignInKey(): Key {
		val secretKey = systemPropertiesVo.jwtSecretKey
		val keyBytes = Decoders.BASE64.decode(secretKey)
		return Keys.hmacShaKeyFor(keyBytes)
	}
}