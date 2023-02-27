package org.http4k.connect.kafka.rest

import org.http4k.client.JavaHttpClient
import org.http4k.connect.assumeDockerDaemonRunning
import org.http4k.core.Uri

class LocalKafkaRestTest : KafkaRestContract() {
    init {
        assumeDockerDaemonRunning()
    }

//    @Disabled("no schema registry available in compose")
//    override fun `can send AVRO messages and get them back`() {
//        super.`can send AVRO messages and get them back`()
//    }

    override val uri = Uri.of("http://localhost:38082")

    override val http = JavaHttpClient()
}
