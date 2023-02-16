package org.http4k.connect.kafka.httpproxy.model

import org.http4k.connect.model.Base64Blob
import se.ansman.kotshi.JsonSerializable

sealed interface Record<K : Any, V : Any> {
    val key: K?
    val `value`: V
    val partition: PartitionId?
}

@JsonSerializable
data class JsonRecord<K : Any, V : Any>(
    override val key: K?,
    override val value: V,
    override val partition: PartitionId? = null
) : Record<K, V>

@JsonSerializable
data class BinaryRecord<K : Any>(
    override val key: K?,
    override val value: Base64Blob,
    override val partition: PartitionId? = null
) : Record<K, Base64Blob>

@JsonSerializable
data class AvroRecord<K : Any, V : Any>(
    override val key: K?,
    override val value: V,
    override val partition: PartitionId? = null
) : Record<K, V>


