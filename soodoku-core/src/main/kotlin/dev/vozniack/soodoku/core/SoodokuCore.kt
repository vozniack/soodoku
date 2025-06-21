package dev.vozniack.soodoku.core

import dev.vozniack.soodoku.core.internal.config.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
@EnableConfigurationProperties(JwtConfig::class)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class SoodokuCore

fun main(args: Array<String>) {
	runApplication<SoodokuCore>(*args)
}
