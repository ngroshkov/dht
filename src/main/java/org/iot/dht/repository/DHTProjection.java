package org.iot.dht.repository;

import org.iot.dht.data.DHT;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "dht", types = {DHT.class})
public interface DHTProjection {
    Long getTimestamp();
    Double getTemperature();
    Double getHumidity();
}