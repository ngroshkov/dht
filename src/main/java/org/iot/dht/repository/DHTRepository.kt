package org.iot.dht.repository

import org.iot.dht.data.DHT
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */
@EnableScan
@RepositoryRestResource(collectionResourceRel = "dhts", path = "dhts")
interface DHTRepository : Repository<DHT, Long> {

    fun findByTimestamp(@Param("timestamp") timestamp: Long?): Optional<DHT>
    fun findAll(): Collection<DHT>

}