import logging
import Adafruit_GPIO as GPIO

class McpInput(object):

    def __init__(self, pin, mcp):
        self.pin = pin
        self.mcp = mcp
        self.mcp.setup(pin, GPIO.IN)
        self.mcp.pullup(pin, True)
        self.logger = logging.getLogger("mcp input")
        self.logger.info("input has been created on pin %d" % self.pin)

    def is_not_closed(self):
        return self.mcp.input(self.pin)
