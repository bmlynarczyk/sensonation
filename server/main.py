from application.LoggerConfiguration import LoggerConfiguration
from domain.Blinds import Blinds
import cherrypy
from cherrypy.process.plugins import Monitor
import logging
import logging.config
import os
from interfaces.Dispatcher import Dispatcher

logging.config.dictConfig(LoggerConfiguration.VALUE)
blinds = Blinds()

cherrypy.engine.unsubscribe('graceful', cherrypy.log.reopen_files)

cherrypy.quickstart(Dispatcher(blinds), config={
    '/': {
            'tools.staticdir.on': True,
            'tools.staticdir.dir': os.path.abspath(os.path.dirname(__file__)) + '/ui',
            'tools.staticdir.index': 'index.html',
            'log.screen': False,
            'log.access_file': '',
            'log.error_file': ''
        },
})
