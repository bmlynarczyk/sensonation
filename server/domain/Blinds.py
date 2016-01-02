from domain.ArduinoState import ArduinoState
from domain.Blind import Blind
import logging
import re
import time


class Blinds(object):

    def __init__(self, serial, arduino):
        self.arduino = arduino
        self.list = [
            Blind('a', 'b', 'c', serial, arduino),
            Blind('b', 'd', 'e', serial, arduino)
        ]

    def pull_down(self):
        if self.arduino.state == ArduinoState.READY_TO_GO:
            for blind in self.list:
                blind.pull_down()
                while self.arduino.state == ArduinoState.STOP_ONLY:
                    time.sleep(1)

    def pull_up(self):
        if self.arduino.state == ArduinoState.READY_TO_GO:
            for blind in self.list:
                blind.pull_up()
                while self.arduino.state == ArduinoState.STOP_ONLY:
                    time.sleep(1)

    def fire_action(self, action_name):
        action_name = self.convert_to_underscore(action_name)
        action_function = getattr(self, action_name)
        action_function()

    def convert_to_underscore(self, name):
        s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
