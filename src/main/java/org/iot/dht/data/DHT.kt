package org.iot.dht.data

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */

@DynamoDBTable(tableName = "IoTData")
class DHT {

    @DynamoDBHashKey
    var timestamp: Long? = null

    @DynamoDBAttribute
    var temperature: Double? = null

    @DynamoDBAttribute
    var humidity: Double? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DHT

        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        return timestamp?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "DHT(timestamp=$timestamp, temperature=$temperature, humidity=$humidity)"
    }

}
