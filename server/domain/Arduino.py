from domain.ArduinoState import ArduinoState


class Arduino(object):

    def __init__(self):
        self.state = ArduinoState.READY_TO_GO
