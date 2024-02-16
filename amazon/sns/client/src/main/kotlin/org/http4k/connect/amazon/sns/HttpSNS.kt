package org.http4k.connect.amazon.sns

import org.http4k.client.JavaHttpClient
import org.http4k.cloudnative.env.Environment
import org.http4k.connect.amazon.AWS_REGION
import org.http4k.connect.amazon.CredentialsProvider
import org.http4k.connect.amazon.Environment
import org.http4k.connect.amazon.core.model.Region
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.Payload.Mode.Signed
import java.time.Clock

/**
 * Standard HTTP implementation of SNS
 */
fun SNS.Companion.Http(
    region: Region,
    credentialsProvider: CredentialsProvider,
    http: HttpHandler = JavaHttpClient(),
    clock: Clock = Clock.systemUTC(),
    overrideEndpoint: Uri? = null,
) = object : SNS {
    private val signedHttp = signAwsRequests(region, credentialsProvider, clock, Signed, overrideEndpoint).then(http)

    override fun <R> invoke(action: SNSAction<R>) = action.toResult(signedHttp(action.toRequest()))
}

/**
 * Convenience function to create a SNS from a System environment
 */
fun SNS.Companion.Http(
    env: Map<String, String> = System.getenv(),
    http: HttpHandler = JavaHttpClient(),
    clock: Clock = Clock.systemUTC(),
    overrideEndpoint: Uri? = null,
    credentialsProvider: CredentialsProvider = CredentialsProvider.Environment(env)
) = Http(Environment.from(env), http, clock, overrideEndpoint, credentialsProvider)

/**
 * Convenience function to create a SNS from an http4k Environment
 */
fun SNS.Companion.Http(
    env: Environment,
    http: HttpHandler = JavaHttpClient(),
    clock: Clock = Clock.systemUTC(),
    overrideEndpoint: Uri? = null,
    credentialsProvider: CredentialsProvider = CredentialsProvider.Environment(env)
) = Http(AWS_REGION(env), credentialsProvider, http, clock, overrideEndpoint)
