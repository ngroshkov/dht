##### Simple home IoT project of telemetering temperature and humidity data
   
Used hardware, services and technologies: DHT22 sensor, Arduino Uno, ESP8266-01, AWS IoT, AWS DynamoDB, AWS ESC,
AWS Elastic Beanstalk, Spring Boot 2, D3.

## Description
ESP9266 firmware request data once per 30 min via serial port from the DHT22 sensor connected to the Arduino Uno 
then publish it to the MQTT device shadow update topic of the AWS IoT server. From there sensor data is streamed 
to the AWS DynanoDB table. Than it is requested by the simple web application (based on Spring Boot) deployed on 
AWS Elastic Beanstalk cloud service.

Page: http://dht22.eu-west-1.elasticbeanstalk.com

## Hardware

### Assemble the circuit
![Alt text](images/esp8266_firmware_upload_mode.png?raw=true "ESP8266 Firmware Upload Mode")

![Alt text](images/work_mode.png?raw=true "Work Mode")

![Alt text](images/photo.jpg?raw=true "Work Mode")

### Load the code

To establish a connection with AWS server:

1. Download **-private.pem.key** and **-certificate.pem.crt** of registered thing 
and AWS IoT CA certificate ([RSA 2048 bit key: Amazon Root CA 1](https://docs.aws.amazon.com/iot/latest/developerguide/managing-device-certs.html))

2. Converting them to DER format:

`openssl rsa -in *-private.pem.key -out ./certificates/private.der -outform DER`  
`openssl x509 -in *-certificate.pem.crt -out ./certificates/cert.der -outform DER`  
`openssl x509 -in "AmazonRootCA1.pem" -out ./certificates/ca.der -outform DER`

3. Copy result *.der files to sketch /data folder and upload it to SPIFFS using [arduino-esp8266fs-plugin
](https://github.com/esp8266/arduino-esp8266fs-plugin)

4. Then upload the code contained in this sketch on to your ESP8266 board

### Folder structure

<pre>
....  
 esp8266_aws_serial_read                => Arduino sketch folder  
  ├── esp8266_aws_serial_read.ino       => main Arduino file  
  └── data                              => folder to store *.der files  
        ├── private.der                 => *.der file from *-private.pem.key convertion  
        ├── cert.der                    => *.der file from *-certificate.pem.crt convertion  
        └── ca.der                      => *.der file from AmazonRootCA1.pem convertion  
....
</pre>

## Software

- Kotlin
- Spring Boot
- Spring-Data-DynamoDB
- Docker 
- AWS Elastic Container Services
- AWS Elastic Beanstalk

Commands to upload container on AWS ECS:

```
mvn install dockerfile:build

docker tag iot/dht:latest 917900149257.dkr.ecr.eu-west-1.amazonaws.com/iot/dht:latest

docker push 917900149257.dkr.ecr.eu-west-1.amazonaws.com/iot/dht:latest

$(aws ecr get-login --no-include-email --region eu-west-1)
```

After run redeploy on AWS EB