/*
  Yun HTTP Client
 
 This example for the Arduino Yún shows how create a basic 
 HTTP client that connects to the internet and downloads 
 content. In this case, you'll connect to the Arduino 
 website and download a version of the logo as ASCII text.

 created by Tom igoe
 May 2013

 This example code is in the public domain.

 http://arduino.cc/en/Tutorial/HttpClient

 */

#include <Bridge.h>
#include <HttpClient.h>
#include <Stepper.h>
 
int in1Pin = 12;
int in2Pin = 11;
int in3Pin = 10;
int in4Pin = 9;
 
Stepper motor(768, in1Pin, in2Pin, in3Pin, in4Pin);
int flag=0;
int value=0;
String preString="0";
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup() {
  pinMode(in1Pin, OUTPUT);
  pinMode(in2Pin, OUTPUT);
  pinMode(in3Pin, OUTPUT);
  pinMode(in4Pin, OUTPUT);
 
  // this line is for Leonardo's, it delays the serial interface
  // until the terminal window is opened
  while (!Serial);
  
  Serial.begin(9600);
  motor.setSpeed(20);
  
  // Bridge takes about two seconds to start up
  // it can be helpful to use the on-board LED
  // as an indicator for when it has initialized  
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);
  Bridge.begin();
  digitalWrite(13, HIGH);
  
  Serial.begin(9600);
  
  while(!Serial); // wait for a serial connection 
}

void loop() {
   if (Serial.available())
  {
    int steps = Serial.parseInt();
     Serial.println("type:"+steps);
    motor.step(steps);
  }
  // Initialize the client library
  HttpClient client;
  
  // Make a HTTP request:
   //client.get("http://arduino.cc/asciilogo.txt");
/*  client.get("http://172.20.10.2:8080/homework/action.jsp?action=SET_VALUE_FROM_ARDUINO&input="+String(flag));
  
  // if there are incoming bytes available 
  // from the server, read them and print them:  
  while (client.available()) {
    char c = client.read();
    //Serial.print(c);
  }
    Serial.flush();
  delay(1000);*/
  client.get("http://172.20.10.2:8080/homework/action.jsp?action=GET_VALUE_FROM_ANDROID");
  while (client.available()) {
    char c = (char)client.read();
   inputString+=c;
   if (c == '\n') {
     Serial.println(inputString);
     stringComplete = true;
     if(inputString!=preString){
       int steps=stringToNumber(inputString);
       //steps=steps/100;
       Serial.println(steps); 
       motor.step(steps);
       preString=inputString;
     }
      // clear the string:
      inputString = "";
      stringComplete = false;
   }
  }
  Serial.flush();
  flag++;
  delay(1000);
  //Serial.println();
}
int stringToNumber(String thisString) {
  int i, value, length;
  length = thisString.length();
  char blah[(length+1)];
  for(i=0; i<length; i++) {
    blah[i] = thisString.charAt(i);
  }
  blah[i]=0;
  value = atoi(blah);
  return value;
}

