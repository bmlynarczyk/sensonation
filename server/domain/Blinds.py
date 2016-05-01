from domain.ArduinoState import ArduinoState
from domain.Blind import Blind
from domain.BlindWithMcp import BlindWithMcp
from domain.McpOutput import McpOutput
from domain.McpInput import McpInput
import logging
import re
import time
from Adafruit_GPIO.MCP230xx import MCP23017


class Blinds(object):

    def __init__(self, arduino):
        mcpA = MCP23017(address=0x20)
        self.list = [
            BlindWithMcp('a', McpOutput(8, mcpA), McpOutput(9, mcpA), McpInput(7, mcpA), McpInput(6, mcpA)),
            BlindWithMcp('b', McpOutput(10, mcpA), McpOutput(11, mcpA), McpInput(5, mcpA), McpInput(4, mcpA)),
            BlindWithMcp('c', McpOutput(12, mcpA), McpOutput(13, mcpA), McpInput(3, mcpA), McpInput(2, mcpA))
        ]

    def pull_down(self):
        for blind in self.list:
            blind.fire_action('pullDown')
            time.sleep(1)

    def pull_up(self):
        for blind in self.list:
            blind.fire_action('pullUp')
            time.sleep(1)

    def fire_action(self, action_name):
        action_name = self.convert_to_underscore(action_name)
        action_function = getattr(self, action_name)
        action_function()

    def convert_to_underscore(self, name):
        s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
