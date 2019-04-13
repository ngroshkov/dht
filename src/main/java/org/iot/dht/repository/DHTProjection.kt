package org.iot.dht.repository

import org.iot.dht.data.DHT
import org.springframework.data.rest.core.config.Projection

@Projection(name = "dht", types = [DHT::class])
interface DHTProjection {
    val timestamp: Long
    val temperature: Double
    val humidity: Double
}