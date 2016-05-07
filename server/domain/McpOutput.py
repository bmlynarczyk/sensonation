import logging
import Adafruit_GPIO as GPIO

class McpOutput(object):

    def __init__(self, pin, mcp):
        self.pin = pin
        self.mcp = mcp
        self.mcp.setup(pin, GPIO.OUT)
        self.logger = logging.getLogger("mcp output")
        self.logger.info("output has been created on pin %d" % self.pin)

    def set_low_state(self):
        self.mcp.output(self.pin, 0)

    def set_high_state(self):
        self.mcp.output(self.pin, 1)
