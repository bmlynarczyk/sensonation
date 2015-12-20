/* Sample Application: Interrupt resetting a loop process

Purpose: This example starts with a process that simply writes a counter to the serial port.
Without the timer enabled, it will run to over 50 and then reset back to 0.
With the timer enabled, it stops under 10 and restarts at the top of the loop (new counter).

*/
//--- Using timer interrupt for this example
#include <TimerOne.h>

//--- inReset is set by the interrupt to tell main loop to stop
boolean inReset = false;

//--- temp counter used by timer process
int timerCounter = 0;

//--- timer process, sample code kicked by interrupt
void Timer1Demo(){
 timerCounter++;
 if( timerCounter > 10 ){
   timerCounter = 0;
   //--- timer process sets global variable, which will
   //    trigger the loop to stop what it is doing and start over

   // Important: If your loop has more than one process in it's own loop
   //  then they all must have a check for inReset.
   inReset = true;
 }
 //... whatever else your interrupt may do
}

void setup() {
 Serial.begin(19200); //or whatever you normally use, maybe 9600 default if not showing in monitor

 //Create sample timer interrupt and attach to sample routine
 Timer1.initialize(50000);
 Timer1.attachInterrupt(Timer1Demo);
}


void loop(){
 int tmpCounter = 0;
 unsigned long check;
 check = millis();
 //Sample program had while statement based on pin value - here just true for this example
 while (true){
   //Sample program had none delay timer, perfect to add the reset switch to
   //Notice the || inReset - this checks for the global flag and ends just like the timer would
   if( (millis() - check) > 6000 || inReset){
   //If the reason for the reset was the flag being set - reset it back
     if( inReset )
       inReset = false;
     break;
   } else {
     //Take some demo action
      Serial.println(tmpCounter++,DEC);
      delay(100);
   }
 }
}
