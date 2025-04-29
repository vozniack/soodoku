package dev.vozniack.soodoku.core.internal.security

import dev.vozniack.soodoku.core.internal.exception.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.nio.charset.StandardCharsets
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(private val jwtSecret: String) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        takeIf { !(request.requestURL.contains("api/auth") || request.requestURL.contains("actuator/health")) }?.let {
            val token = parseJwtToken(request) ?: throw UnauthorizedException("Token is not present")
            val loggedUser = buildLoggedUser(token) ?: throw UnauthorizedException("Token is not valid")

            SecurityContextHolder.getContext().authentication = loggedUser
        }.also { filterChain.doFilter(request, response) }
    }

    private fun parseJwtToken(request: HttpServletRequest): String? = request.getHeader("Authorization")
        .takeIf { it != null && it.startsWith("Bearer ") && it.split(" ").size == 2 }?.substring(7)

    private fun buildLoggedUser(token: String): UsernamePasswordAuthenticationToken? {
        val parsedToken: Jws<Claims> = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)

        val email: String = parsedToken.payload.subject ?: return null
        val authorities = listOf(SimpleGrantedAuthority("USER"))

        return UsernamePasswordAuthenticationToken(User(email, "password", authorities), email, authorities)
    }
}
