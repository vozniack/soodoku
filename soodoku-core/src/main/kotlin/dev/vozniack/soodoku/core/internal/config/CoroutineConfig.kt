package dev.vozniack.soodoku.core.internal.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig : DisposableBean {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    @Bean
    fun coroutineScope(): CoroutineScope = scope

    override fun destroy() {
        job.cancel()
    }
}
