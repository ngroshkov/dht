package org.iot.dht

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

/**
 * Created by Nikolay Groshkov on 24-Mar-2019.
 */
@SpringBootApplication(
        scanBasePackages = arrayOf("org.iot.dht"),
        exclude = arrayOf(SecurityAutoConfiguration::class)
)
class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

}
