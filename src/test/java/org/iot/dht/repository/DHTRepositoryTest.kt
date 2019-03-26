package org.iot.dht.repository

import org.iot.dht.configuration.DatabaseConfiguration
import org.iot.dht.data.DHT
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.function.Consumer

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [PropertyPlaceholderAutoConfiguration::class, DatabaseConfiguration::class])
class DHTRepositoryTest {

    @Autowired
    private lateinit var dhtRepository: DHTRepository

    @Test
    fun testDatabaseAccess() {
        val dhts = dhtRepository.findAll()
        assertFalse(dhts.isEmpty())
        dhts.forEach(Consumer<DHT> { println(dhts) })
    }
}
