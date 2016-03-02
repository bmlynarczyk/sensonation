from application.StopperCallback import StopperCallback
from application.LoggerConfiguration import LoggerConfiguration
from domain.Arduino import Arduino
from domain.Blinds import Blinds
import cherrypy
from cherrypy.process.plugins import Monitor
import logging
import logging.config
from interfaces.BlindController import BlindController
from interfaces.BlindsController import BlindsController
from interfaces.TasksController import TasksController
import os
import serial


class Root(object):
    pass

if __name__ == '__main__':
    logging.config.dictConfig(LoggerConfiguration.VALUE)
    ser = serial.Serial('/dev/ttyAMA0', 9600)
    # ser = Arduino()
    arduino = Arduino()

    blinds = Blinds(ser, arduino)

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
    cherrypy.tree.mount(
        TasksController(blinds, arduino),
        '/api/tasks',
        {'/': {'request.dispatch': cherrypy.dispatch.MethodDispatcher()}}
    )
    cherrypy.tree.mount(Root(), '/', config={
        '/': {
                'tools.staticdir.on': True,
                'tools.staticdir.dir': os.path.abspath(os.path.dirname(__file__)) + '/ui',
                'tools.staticdir.index': 'index.html',
                'log.screen': False,
                'log.access_file': '',
                'log.error_file': ''
            },
    })
    cherrypy.engine.unsubscribe('graceful', cherrypy.log.reopen_files)

    Monitor(
        cherrypy.engine,
        StopperCallback(ser, arduino),
        frequency=1
    ).subscribe()

    cherrypy.engine.start()

    cherrypy.engine.block()
