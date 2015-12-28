from BlindView import BlindView
import cherrypy
import json
import logging

__author__ = 'bmlynarczyk'


class BlindController(object):

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds
        self.logger = logging.getLogger("BlindController")

    def _cp_dispatch(self, vpath):
        if len(vpath) == 3:
            cherrypy.request.params['name'] = vpath.pop(0)
            vpath.pop(0)
            cherrypy.request.params['action_name'] = vpath.pop(0)
            return self
        return vpath

    def PUT(self, name, action_name):
        for blind in self.blinds:
            if blind.name == name:
                try:
                    blind.fire_action(action_name)
                    cherrypy.response.status = 200
                    return json.dumps(BlindView(blind.name).__dict__)
                except ValueError:
                    cherrypy.response.status = 409
                    return
        else:
            cherrypy.response.status = 404
