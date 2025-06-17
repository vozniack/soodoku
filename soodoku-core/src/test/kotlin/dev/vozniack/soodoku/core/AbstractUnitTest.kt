package dev.vozniack.soodoku.core

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["unit-test"])
abstract class AbstractUnitTest {

    protected fun authenticate(email: String?) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            email, null, emptyList()
        )
    }
}
