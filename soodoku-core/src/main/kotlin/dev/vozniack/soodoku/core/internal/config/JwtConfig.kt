package dev.vozniack.soodoku.core.internal.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "soodoku.security.jwt")
data class JwtConfig(
    val secret: String = "",
    val expiration: Long = 0
)
