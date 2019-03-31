package org.iot.dht.configuration

import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.File
import java.nio.file.Paths

/**
 * Created by Nikolay Groshkov on 27-Mar-2019.
 */
@Configuration
@ComponentScan(basePackages = arrayOf("org.iot.dht.service"))
@PropertySource("classpath:aws.properties")
class AWSConfiguration {

    @Value("\${amazon.aws.clientEndpoint}")
    private lateinit var amazonAWSClientEndpoint: String

    @Value("\${amazon.aws.certificate}")
    private lateinit var certificateFile: String

    @Value("\${amazon.aws.privateKey}")
    private lateinit var privateKeyFile: String

    @Bean(initMethod = "connect", destroyMethod = "disconnect")
    fun AWSIotMqttClient(): AWSIotMqttClient {
        val certificate = javaClass.classLoader.getResource(certificateFile).file;
        val privateKey = javaClass.classLoader.getResource(privateKeyFile).file

        val pair = SampleUtil.getKeyStorePasswordPair(certificate, privateKey)

        return AWSIotMqttClient (amazonAWSClientEndpoint, "Application", pair.keyStore, pair.keyPassword)
    }


}
