import json
import logging
import cherrypy
from BlindView import BlindView

__author__ = 'bmlynarczyk'


class BlindController:

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds
        self.logger = logging.getLogger("BlindController")

    def _cp_dispatch(self, vpath):
        if len(vpath) == 3:
            cherrypy.request.params['name'] = vpath.pop(0)
            vpath.pop(0)
            cherrypy.request.params['actionName'] = vpath.pop(0)
            return self
        return vpath

    def PUT(self, name, actionName):
        for blind in self.blinds:
            if blind.name == name:
                try:
                    blind.fire_action(actionName)
                    cherrypy.response.status = 200
                    return json.dumps(BlindView(blind.name).__dict__)
                except ValueError:
                    cherrypy.response.status = 409
                    return
        else:
            cherrypy.response.status = 404
