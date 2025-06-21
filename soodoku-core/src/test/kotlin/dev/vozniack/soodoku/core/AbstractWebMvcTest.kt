package dev.vozniack.soodoku.core

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@ActiveProfiles(profiles = ["unit-test"])
abstract class AbstractWebMvcTest(context: WebApplicationContext) {

    protected var mockMvc: MockMvc = MockMvcBuilders.webAppContextSetup(context).build()

    protected fun authenticate(email: String?) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            email, null, emptyList()
        )
    }

    @BeforeEach
    fun `clear security context before`() {
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun `clear security context after`() {
        SecurityContextHolder.clearContext()
    }
}
