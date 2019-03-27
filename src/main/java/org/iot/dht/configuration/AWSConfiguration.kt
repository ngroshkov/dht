package org.iot.dht.configuration

import com.amazonaws.services.iot.client.AWSIotMqttClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Created by Nikolay Groshkov on 27-Mar-2019.
 */
@Configuration
@ComponentScan(basePackages = arrayOf("org.iot.dht.service"))
@PropertySource("classpath:aws.properties")
class AWSConfiguration {

    @Value("\${amazon.aws.accesskey}")
    private lateinit var amazonAWSAccessKey: String

    @Value("\${amazon.aws.secretkey}")
    private lateinit var amazonAWSSecretKey: String

    @Value("\${amazon.aws.clientEndpoint}")
    private lateinit var amazonAWSClientEndpoint: String

    @Bean(initMethod = "connect", destroyMethod = "disconnect")
    fun AWSIotMqttClient(): AWSIotMqttClient =
            AWSIotMqttClient(amazonAWSClientEndpoint, "Application", amazonAWSAccessKey, amazonAWSSecretKey)

}
