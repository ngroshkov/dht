package org.iot.dht.configuration

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Created by Nikolay Groshkov on 26-Mar-2019.
 */
@Configuration
@EnableDynamoDBRepositories(
        basePackages = ["org.iot.dht.repository"],
        mappingContextRef = "dynamoDBMappingContext"
)
@PropertySource("classpath:aws.properties")
class DatabaseConfiguration {

    @Value("\${amazon.aws.accesskey}")
    private lateinit var amazonAWSAccessKey: String

    @Value("\${amazon.aws.secretkey}")
    private lateinit var amazonAWSSecretKey: String

    fun amazonAWSCredentialsProvider(): AWSCredentialsProvider = AWSStaticCredentialsProvider(amazonAWSCredentials())

    @Bean
    fun amazonAWSCredentials(): AWSCredentials = BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)

    @Bean
    fun dynamoDBMapperConfig(): DynamoDBMapperConfig = DynamoDBMapperConfig.DEFAULT

    @Bean
    fun dynamoDBMapper(amazonDynamoDB: AmazonDynamoDB, config: DynamoDBMapperConfig): DynamoDBMapper =
            DynamoDBMapper(amazonDynamoDB, config)

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB =
            AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                    .withRegion(Regions.EU_WEST_1).build()

    @Bean
    fun dynamoDBMappingContext(): DynamoDBMappingContext = DynamoDBMappingContext()

}