#include <SoftwareSerial.h>

SoftwareSerial mySerial(0, 1);


int analogValue1 = 0;
int analogValue2 = 0;
int analogValue3 = 0;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  mySerial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  analogValue1 = analogRead(A0);
  analogValue2 = analogRead(A1);
  analogValue3 = analogRead(A2);
  Serial.print(analogValue1);
  Serial.print(" ");
  Serial.print(analogValue2);
  Serial.print(" ");
  Serial.println(analogValue3);
  mySerial.print(analogValue1);
  mySerial.print(" ");
  mySerial.print(analogValue2);
  mySerial.print(" ");
  mySerial.println(analogValue3);
  delay(1000);
}
