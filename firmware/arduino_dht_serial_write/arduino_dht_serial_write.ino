#include <SoftwareSerial.h>
#include "DHT.h"

SoftwareSerial espSerial(2, 3); // TX RX
#define WIFI_SERIAL espSerial
#define DHTTYPE DHT22
#define DHTPIN 4
DHT dht(DHTPIN, DHTTYPE);


void setup()
{
  Serial.begin(9600);
  while (!Serial) {
  }
  Serial.print("Serial init OK\r\n");
  WIFI_SERIAL.begin(115200);

  Serial.println(F("DHT22 test!"));
  dht.begin();
  delay(10000);
}

void loop()
{
  delay(1000);

  if (WIFI_SERIAL.available()) {
    String message = WIFI_SERIAL.readString();
    Serial.println(message);

    if (message == "GET") {
      float h = dht.readHumidity();
      float t = dht.readTemperature();
      if (isnan(h) || isnan(t)) {
        Serial.println("Failed to read from DHT sensor!");
        return;
      }
      String temperature = String(t);
      String humidity = String(h);
      String payload = temperature + ',' + humidity + ';';

      //  Serial.println(payload);
      WIFI_SERIAL.print(payload);
    }
  }
}