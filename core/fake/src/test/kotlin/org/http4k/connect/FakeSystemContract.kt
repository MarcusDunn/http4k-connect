package org.http4k.connect

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.greaterThan
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.I_M_A_TEAPOT
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasContentType
import org.http4k.hamkrest.hasStatus
import org.junit.jupiter.api.Test

abstract class FakeSystemContract(private val fake: ChaosFake) {
    protected abstract val anyValidRequest: Request

    @Test
    fun `can serve the Chaos API`() {
        val response = fake(Request(GET, "/chaos"))
        assertThat(response, hasStatus(OK).and(hasContentType(APPLICATION_JSON)).and(hasBody(containsSubstring("http4k Chaos Engine"))))
    }

    @Test
    fun `returns error when told to misbehave`() {
        val originalStatus = fake(anyValidRequest).status
        fake.returnStatus(I_M_A_TEAPOT)
        assertThat(fake(anyValidRequest), hasStatus(I_M_A_TEAPOT))
        fake.behave()
        assertThat(fake(anyValidRequest), hasStatus(originalStatus))
    }

    @Test
    fun `default port number is suitably random`() {
        assertThat(fake::class.defaultPort(), greaterThan(10000))
        assertThat(fake::class.defaultPort() % 100, greaterThan(0))
    }
}
