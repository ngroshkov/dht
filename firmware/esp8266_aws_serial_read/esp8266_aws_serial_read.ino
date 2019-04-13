#include "FS.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

const char* ssid = "MGTS_GPON_9742";
const char* password = "XKBECV2G";
const char* AWS_endpoint = "a3b1hcxwjovrbh-ats.iot.eu-west-1.amazonaws.com"; //MQTT broker ip
const char* AWS_device_shadow_update_topic = "$aws/things/DHT/shadow/update";

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");
WiFiClientSecure espClient;
PubSubClient client(AWS_endpoint, 8883, espClient); //set  MQTT port number to 8883 as per //standard

long last = 0;
char msg[100];

void setup() {
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  delay(1000);
  pinMode(BUILTIN_LED, OUTPUT);     // Initialize the BUILTIN_LED pin as an output
  setup_wifi();
  setup_cert();
}

void setup_wifi() {
  delay(10);
  espClient.setBufferSizes(512, 512);

  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  timeClient.begin();
  while(!timeClient.update()){
    timeClient.forceUpdate();
  }

  espClient.setX509Time(timeClient.getEpochTime());
}

void setup_cert() {
   if (!SPIFFS.begin()) {
    Serial.println("Failed to mount file system");
    return;
  }

  Serial.print("Heap: "); Serial.println(ESP.getFreeHeap());

  // Load certificate file
  File cert = SPIFFS.open("/cert.der", "r"); //replace cert.crt eith your uploaded file name
  if (!cert) {
    Serial.println("Failed to open cert file");
  }
  else
    Serial.println("Success to open cert file");

  delay(1000);

  if (espClient.loadCertificate(cert))
    Serial.println("cert loaded");
  else
    Serial.println("cert not loaded");

  // Load private key file
  File private_key = SPIFFS.open("/private.der", "r"); //replace private eith your uploaded file name
  if (!private_key) {
    Serial.println("Failed to open private cert file");
  }
  else
    Serial.println("Success to open private cert file");

  delay(1000);

  if (espClient.loadPrivateKey(private_key))
    Serial.println("private key loaded");
  else
    Serial.println("private key not loaded");

  // Load CA file
  File ca = SPIFFS.open("/ca.der", "r"); //replace ca eith your uploaded file name
  if (!ca) {
    Serial.println("Failed to open ca ");
  }
  else
    Serial.println("Success to open ca");
  delay(1000);

  if(espClient.loadCACert(ca))
    Serial.println("ca loaded");
  else
    Serial.println("ca failed");

  Serial.print("Heap: "); Serial.println(ESP.getFreeHeap());
}

int roundSeconds = 1800;
int lastMinute = 0;

void loop() {
  delay(10000);

  timeClient.update();

  int currentMinute = timeClient.getMinutes();
  long timestamp = roundSeconds * (timeClient.getEpochTime() / roundSeconds);
  if (lastMinute != currentMinute) {
    send(currentMinute, timestamp);
  }
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("hello/esp8266", "connect");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");

      char buf[256];
      espClient.getLastSSLError(buf,256);
      Serial.print("WiFiClientSecure SSL error: ");
      Serial.println(buf);

      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void send(int minute, long t) {
  Serial.print("GET");
  delay(2000);

  if (Serial.available()) {
    if (!client.connected()) {
      reconnect();
    }
    client.loop();

    String message = Serial.readString();

    int finIndex = message.indexOf(';');
    message = message.substring(0, finIndex);
    int sepIndex = message.indexOf(',');
    String temperature = message.substring(0, sepIndex);
    String humidity = message.substring(sepIndex + 1);
    String timestamp = String(t);

    String payload = "{\"state\":{\"desired\":{";
    payload += "\"timestamp\":"; payload += timestamp; payload += ",";
    payload += "\"temperature\":"; payload += temperature; payload += ",";
    payload += "\"humidity\":"; payload += humidity;
    payload += "}}}";

    Serial.print(timestamp + " " + temperature + " " + humidity);
    payload.toCharArray( msg, 100 );
    client.publish(AWS_device_shadow_update_topic, msg);
    lastMinute = minute;
  }
}
