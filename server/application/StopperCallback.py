from domain.ArduinoState import ArduinoState
import logging


class StopperCallback(object):

    def __init__(self, serial, arduino):
        self.serial = serial
        self.arduino = arduino
        self.logger = logging.getLogger("StopperCallback")

    def __call__(self):
        self.logger.debug("arduino state is: %d" % self.arduino.state)
        if self.arduino.state == ArduinoState.STOP_ONLY:
            self.logger.info("start waiting in stopperHandler")
            self.serial.readline()
            self.logger.info("stop waiting in stopperHandler")
            self.arduino.state = ArduinoState.READY_TO_GO
