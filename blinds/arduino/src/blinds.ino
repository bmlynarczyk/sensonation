int number = 0;

class Blind{
private:
  int firstInputPin;
  int secondInputPin;
  int firstOutputPin;
  int secondOutputPin;
public:
  Blind(int firstOutputPin, int secondOutputPin, int firstInputPin, int secondInputPin);
  void pullDown();
  void pullUp();
  void stop();
};

Blind::Blind(int firstOutputPin, int secondOutputPin, int firstInputPin, int secondInputPin){
  pinMode(firstOutputPin, OUTPUT);
  pinMode(secondOutputPin, OUTPUT);
  pinMode(firstInputPin, INPUT);
  pinMode(secondInputPin, INPUT);
  digitalWrite(firstInputPin, HIGH);
  digitalWrite(secondInputPin, HIGH);
  this->firstOutputPin = firstOutputPin;
  this->secondOutputPin = secondOutputPin;
  this->firstInputPin = firstInputPin;
  this->secondInputPin = secondInputPin;
}

void Blind::pullDown(){
  digitalWrite(firstOutputPin, LOW);
  delay(500);
  digitalWrite(secondOutputPin, HIGH);
  while(digitalRead(firstInputPin) == HIGH){
    if (Serial.available() && Serial.read() == 97){
      break;
    }
    delay(50);
  }
  this->stop();
}

void Blind::pullUp(){
  digitalWrite(secondOutputPin, LOW);
  delay(500);
  digitalWrite(firstOutputPin, HIGH);
  while(digitalRead(secondInputPin) == HIGH){
    if (Serial.available() && Serial.read() == 97){
      break;
    }
    delay(50);
  }
  this->stop();
}

void Blind::stop(){
  digitalWrite(firstOutputPin, LOW);
  digitalWrite(secondOutputPin, LOW);
  delay(2500);
  Serial.write("stopped\n");
}

Blind blindA = Blind(2, 3, 6, 7);
Blind blindB = Blind(4, 5, 8, 9);
Blind blindC = Blind(22, 24, 23, 25);

void setup() {
  Serial.begin(9600);
}

void loop(){
  if (Serial.available()) {
    number = Serial.read();
    switch(number){
      case 97:
        blindA.stop();
        blindB.stop();
        blindC.stop();
        break;
      case 98:
        blindA.pullDown();
        break;
      case 99:
        blindA.pullUp();
        break;
      case 100:
        blindB.pullDown();
        break;
      case 101:
        blindB.pullUp();
        break;
      case 102:
        blindC.pullDown();
        break;
      case 103:
        blindC.pullUp();
        break;
    }
  }
  delay(200);
}
