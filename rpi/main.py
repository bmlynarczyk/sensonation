import logging

import cherrypy
import serial
from Blind import Blind
from BlindController import BlindController
from BlindsController import BlindsController


if __name__ == '__main__':

    logging.basicConfig(level=logging.DEBUG)

    ser = serial.Serial('/dev/ttyACM0', 9600)

    blinds = [Blind('a', 'b', 'c', ser), Blind('b', 'd', 'e', ser)]

    cherrypy.tree.mount(
        BlindController(blinds), '/api/blind',
        {'/':
             {'request.dispatch': cherrypy.dispatch.MethodDispatcher()}
        }
    )
    cherrypy.tree.mount(
        BlindsController(blinds), '/api/blinds',
        {'/':
             {'request.dispatch': cherrypy.dispatch.MethodDispatcher()}
        }
    )

    cherrypy.engine.start()
    cherrypy.engine.block()
