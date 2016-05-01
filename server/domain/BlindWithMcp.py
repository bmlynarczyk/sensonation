from domain.ArduinoState import ArduinoState
import logging
import re
import time

class BlindWithMcp(object):

    def __init__(self, name, first_output, second_output, first_input, second_input):
        self.name = name
        self.first_output = first_output
        self.second_output = second_output
        self.first_input = first_input
        self.second_input = second_input
        self.logger = logging.getLogger("blind")
        self.active = True
        self.logger.info("blind %s has been created" % self.name)

    def activate(self):
        self.active = True
        self.logger.info("blind %s has been activated" % self.name)

    def deactivate(self):
        self.active = False
        self.logger.info("blind %s has been deactivated" % self.name)

    def stop(self):
        self.logger.info("stop movement of %s" % self.name)
        self.first_output.set_low_state()
        self.second_output.set_low_state()

    def pull_down(self):
        self.logger.info("pull down %s" % self.name)
        self.first_output.set_low_state()
        time.sleep(0.5)
        self.second_output.set_high_state()
        while(self.first_input.is_not_closed()):
            time.sleep(0.05)
        self.stop()

    def pull_up(self):
        self.logger.info("pull up %s" % self.name)
        self.second_output.set_low_state()
        time.sleep(0.5)
        self.first_output.set_high_state()
        while(self.second_input.is_not_closed()):
            time.sleep(0.05)
        self.stop()

    def fire_action(self, action_name):
        action_name = self.convert_to_underscore(action_name)
        if(self.active):
            self.logger.info("call %s" % action_name)
            action_function = getattr(self, action_name)
            action_function()
        elif(action_name == "activate"):
            self.activate()
        else:
            self.logger.info("blind %s is inactive" % self.name)

    def convert_to_underscore(self, name):
        s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()