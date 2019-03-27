package org.iot.dht.service

import org.iot.dht.configuration.AWSConfiguration
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull

/**
 * Created by Nikolay Groshkov on 27-Mar-2019.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [PropertyPlaceholderAutoConfiguration::class, AWSConfiguration::class])
class DHTServiceTest {

    @Autowired
    private lateinit var dhtService: DHTService

    @Test
    fun testDatabaseAccess() {
        val shadow = dhtService.dhtCurrentState()
        assertNotNull(shadow)
        println(shadow)
    }
}
