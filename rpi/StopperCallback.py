import logging
from ArduinoState import ArduinoState

class StopperCallback:

    def __init__(self, serial, arduino):
        self.serial = serial
        self.arduino = arduino
        self.logger = logging.getLogger("StopperCallback")

    def __call__(self):
        self.logger.debug("stopperHandler:arduino state is: %d" % self.arduino.state)
        if self.arduino.state == ArduinoState.STOP_ONLY :
            self.logger.debug("start waiting in stopperHandler")
            self.serial.readline()
            self.logger.debug("stop waiting in stopperHandler")
            self.arduino.state = ArduinoState.READY_TO_GO
