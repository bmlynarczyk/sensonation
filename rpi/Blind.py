__author__ = 'bmlynarczyk'

import logging
from ArduinoState import ArduinoState


class Blind:

    def __init__(self, name, pull_down_code, pull_up_code, serial, arduino):
        self.name = name
        self.stop_code = 'a'
        self.pull_down_code = pull_down_code
        self.pull_up_code = pull_up_code
        self.serial = serial
        self.logger = logging.getLogger("blind")
        self.arduino = arduino

    def stop(self):
        if(self.arduino.state == ArduinoState.STOP_ONLY):
            self.logger.debug("stop movement of %s" % self.name)
            self.serial.write(self.stop_code)
        else:
            raise ValueError("blind is stopped")

    def pullDown(self):
        if(self.arduino.state == ArduinoState.READY_TO_GO):
            self.logger.debug("pull down %s" % self.name)
            self.serial.write(self.pull_down_code)
            self.arduino.state = ArduinoState.STOP_ONLY
        else:
            raise ValueError("blind is moving")

    def pullUp(self):
        if(self.arduino.state == ArduinoState.READY_TO_GO):
            self.logger.debug("pull up %s" % self.name)
            self.serial.write(self.pull_up_code)
            self.arduino.state = ArduinoState.STOP_ONLY
        else:
            raise ValueError("blind is moving")

    def fire_action(self, action_name):
        self.logger.debug("call %s" % action_name)
        action_function = getattr(self, action_name)
        action_function()
