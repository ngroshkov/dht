package org.iot.dht.repository

import org.iot.dht.data.DHT
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */
@EnableScan
@RepositoryRestResource(
        collectionResourceRel = "dhts", path = "dhts",
        excerptProjection = DHTProjection::class)
interface DHTRepository : Repository<DHT, Long> {

    fun findAll(): Collection<DHT>
    fun findByTimestampIsBetween(@Param("start") start: Long, @Param("end") end: Long): Collection<DHT>

}