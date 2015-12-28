from Arduino import Arduino
from Blind import Blind
from BlindController import BlindController
from BlindsController import BlindsController
import cherrypy
from cherrypy.process.plugins import Monitor
import logging
import serial
from StopperCallback import StopperCallback


if __name__ == '__main__':

    logging.basicConfig(level=logging.INFO)

    ser = serial.Serial('/dev/ttyACM0', 9600)
    arduino = Arduino()

    blinds = [
        Blind('a', 'b', 'c', ser, arduino),
        Blind('b', 'd', 'e', ser, arduino)
    ]

    cherrypy.tree.mount(
        BlindController(blinds),
        '/api/blind',
        {'/': {'request.dispatch': cherrypy.dispatch.MethodDispatcher()}}
    )
    cherrypy.tree.mount(
        BlindsController(blinds),
        '/api/blinds',
        {'/': {'request.dispatch': cherrypy.dispatch.MethodDispatcher()}}
    )

    Monitor(
        cherrypy.engine,
        StopperCallback(ser, arduino),
        frequency=1
    ).subscribe()

    cherrypy.engine.start()

    cherrypy.engine.block()
