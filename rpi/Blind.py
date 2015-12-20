__author__ = 'bmlynarczyk'

import logging

class Blind:

    def __init__(self, name, pull_down_code, pull_up_code, serial):
        self.name = name
        self.stopCode = 'a'
        self.pull_down_code = pull_down_code
        self.pull_up_code = pull_up_code
        self.serial = serial
        self.logger = logging.getLogger("blind")

    def stop(self):
        self.logger.debug("stop movement of %s" % self.name)
        self.serial.write(self.stopCode)

    def pullDown(self):
        self.logger.debug("pull down %s" % self.name)
        self.serial.write(self.pull_down_code)

    def pullUp(self):
        self.logger.debug("pull up %s" % self.name)
        self.serial.write(self.pull_up_code)

    def fire_action(self, action_name):
        self.logger.debug("call %s" % action_name)
        action_function = getattr(self, action_name)
        action_function()
