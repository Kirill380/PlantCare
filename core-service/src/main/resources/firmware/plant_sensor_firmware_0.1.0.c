#include <ESP8266WiFi.h>

const char* ssid = "<your_wifi_ssid>";
const char* password = "<your_wifi_password>";
const char* server = "<ip_of_plant_care_server>";

const int redPin = 4; //  ~D2
const int greenPin = 12; // ~D6
const int bluePin = 14; // ~D5


int WiFiStrength = 0;


void setup() {
  Serial.begin(115200);
  delay(10);

  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);


  // color while waiting to connect
  analogWrite(redPin, 280);
  analogWrite(greenPin, 300);
  analogWrite(bluePin, 300);


  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);

  // connect to WiFi router
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");

  // Print the IP address
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");

}

double analogValue = 0.0;
double analogVolts = 0.0;
unsigned long timeHolder = 0;


void loop() {

  WiFiStrength = WiFi.RSSI(); // get dBm from the ESP8266
  analogValue = analogRead(A0); // read the analog signal

  // convert the analog signal to voltage
  // the ESP2866 A0 reads between 0 and ~3 volts, producing a corresponding value
  // between 0 and 1024. The equation below will convert the value to a voltage value.

  analogVolts = (analogValue * 3.08) / 1024;

  // now get our chart value by converting the analog (0-1024) value to a value between 0 and 100.
  // the value of 400 was determined by using a dry moisture sensor (not in soil, just in air).
  // When dry, the moisture sensor value was approximately 400. This value might need adjustment
  // for fine tuning of the chartValue.

  int chartValue = (analogValue * 100) / 964;

  // now reverse the value so that the value goes up as moisture increases
  // the raw value goes down with wetness, we want our chart to go up with wetness
  chartValue = 100 - chartValue;

  // set a "blink" time interval in milliseconds.
  // for example, 15000 is 15 seconds. However, the blink will not always be 15 seconds due to other
  // delays set in the code.

  if (millis() - 3000 > timeHolder)
  {
    timeHolder = millis();

    // determine which color to use with the LED based on the chartValue.
    // note: PWM is used so any color combo desired can be set by changing the values sent to each pin
    // between 0 and 1024 - 0 being OFF and 1024 being full power

    if (chartValue <= 25)
    {  // 0-25 is red "dry"
      analogWrite(redPin, 1000);
      analogWrite(greenPin, 0);
      analogWrite(bluePin, 0);

    }
    else if (chartValue > 25 && chartValue <= 75) // 26-75 is yellow
    {
      analogWrite(redPin, 900);
      analogWrite(greenPin, 100);
      analogWrite(bluePin, 0);
    }
    else if (chartValue > 75 ) // 76-100 is green
    {
      analogWrite(redPin, 0);
      analogWrite(greenPin, 1000);
      analogWrite(bluePin, 0);
    }

    delay(1000); // this is the duration the LED will stay ON

    analogWrite(redPin, 0);
    analogWrite(greenPin, 0);
    analogWrite(bluePin, 0);
  }

  // Serial data
  Serial.print("Analog raw: ");
  Serial.println(analogValue);
  Serial.print("Analog V: ");
  Serial.println(analogVolts);
  Serial.print("ChartValue: ");
  Serial.println(chartValue);
  Serial.print("TimeHolder: ");
  Serial.println(timeHolder);
  Serial.print("millis(): ");
  Serial.println(millis());
  Serial.print("WiFi Strength: ");
  Serial.print(WiFiStrength); Serial.println("dBm");
  Serial.println(" ");

  WiFiClient client;
  Serial.printf("\n[Connecting to %s ... ", server);
  if(client.connect(server, 8081))
  {
    Serial.println("connected]");

    Serial.println("[Sending a request]");
    String payload = String("{\"endpointId\" : \"sensor1\", \"dataType\" : \"moisture\", \"value\" : ") + chartValue + "}";
    client.println("POST /endpoints/data HTTP/1.1");
    client.println(String("Host: ") + server);
    client.println("Content-Type: application/json");
    client.println("Connection: close");
    client.println(String("Content-Length: ") + payload.length());
    client.println();
    client.print(payload);

    Serial.println("[Response:]");
    while (client.connected())
    {
      if (client.available())
      {
        String line = client.readStringUntil('\n');
        Serial.println(line);
      }
    }
    client.stop();
    Serial.println("\n[Disconnected]");
  }
  else
  {
    Serial.println("connection failed!]");
    client.stop();
  }

 delay(1000);
}