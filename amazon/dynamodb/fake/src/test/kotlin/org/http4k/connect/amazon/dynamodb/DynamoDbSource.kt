package org.http4k.connect.amazon.dynamodb

import org.http4k.connect.amazon.fakeAwsEnvironment
import org.http4k.connect.assumeDockerDaemonRunning
import org.http4k.core.Uri
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

interface DynamoDbSource {
    val dynamo: DynamoDb
}

class FakeDynamoDbSource : DynamoDbSource {
    override val dynamo = DynamoDb.Http(
        fakeAwsEnvironment.region,
        { fakeAwsEnvironment.credentials },
        FakeDynamoDb()
    )
}

class LocalDynamoDbSource : DynamoDbSource {
    init {
        assumeDockerDaemonRunning()
    }

    private val container: GenericContainer<*> = GenericContainer(DockerImageName.parse("amazon/dynamodb-local:1.15.0"))
        .withExposedPorts(8000)
        .also { it.start() }

    override val dynamo = DynamoDb.Http(
        fakeAwsEnvironment.region,
        { fakeAwsEnvironment.credentials },
        overrideEndpoint = Uri.of("http://localhost:${container.getMappedPort(8000)}")
    )
}
