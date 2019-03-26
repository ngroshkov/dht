package org.iot.dht.repository

import org.iot.dht.data.DHT
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */
@EnableScan
interface DHTRepository : CrudRepository<DHT, Long> {

    fun findByTimestamp(timestamp: Long?): Optional<DHT>
    override fun findAll(): Collection<DHT>

}