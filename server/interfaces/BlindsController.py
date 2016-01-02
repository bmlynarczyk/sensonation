from interfaces.views.BlindView import BlindView
import cherrypy
import json


class BlindsController(object):

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds

    def _cp_dispatch(self, vpath):
        if len(vpath) == 1:
            cherrypy.request.params['action_name'] = vpath.pop(0)
            return self
        return vpath

    def GET(self):
        views = []
        for blind in self.blinds.list:
            views.append(BlindView(blind.name).__dict__)
        return json.dumps(views)

    def PUT(self, action_name):
        self.blinds.fire_action(action_name)
