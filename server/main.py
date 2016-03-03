from application.StopperCallback import StopperCallback
from application.LoggerConfiguration import LoggerConfiguration
from domain.Arduino import Arduino
from domain.Blinds import Blinds
import cherrypy
from cherrypy.process.plugins import Monitor
import logging
import logging.config
import serial
import os
from interfaces.Dispatcher import Dispatcher

logging.config.dictConfig(LoggerConfiguration.VALUE)
ser = serial.Serial('/dev/ttyAMA0', 9600)
# ser = Arduino
arduino = Arduino()
blinds = Blinds(ser, arduino)
Monitor(
    cherrypy.engine,
    StopperCallback(ser, arduino),
    frequency=1
).subscribe()

cherrypy.engine.unsubscribe('graceful', cherrypy.log.reopen_files)

cherrypy.quickstart(Dispatcher(arduino, blinds), config={
    '/': {
            'tools.staticdir.on': True,
            'tools.staticdir.dir': os.path.abspath(os.path.dirname(__file__)) + '/ui',
            'tools.staticdir.index': 'index.html',
            'log.screen': False,
            'log.access_file': '',
            'log.error_file': ''
        },
})
