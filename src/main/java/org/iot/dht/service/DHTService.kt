package org.iot.dht.service

import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotException
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by Nikolay Groshkov on 27-Mar-2019.
 */
@Service
class DHTService {

    @Autowired
    private lateinit var client: AWSIotMqttClient

    @Throws(AWSIotException::class)
    internal fun dhtCurrentState(): String {
        val device = AWSIotDevice("DHT")
        client.attach(device)
        val state = device.get()
        client.detach(device);
        val mapper = ObjectMapper()
        val node = mapper.readTree(state)
        return node.findValue("state").findValue("desired").toString()
    }
}
