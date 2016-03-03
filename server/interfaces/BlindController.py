from interfaces.views.BlindView import BlindView
import json
import logging


class BlindController(object):

    def __init__(self, blinds):
        self.blinds = blinds
        self.logger = logging.getLogger("BlindController")

    def PUT(self, cherrypy):
        input_json = cherrypy.request.json
        try:
            name = input_json['name']
            action_name = input_json['action_name']
        except KeyError:
            cherrypy.response.status = 400
            return
        for blind in self.blinds.list:
            if blind.name == name:
                try:
                    blind.fire_action(action_name)
                    cherrypy.response.status = 200
                    return BlindView(blind.name, blind.active).__dict__
                except ValueError:
                    cherrypy.response.status = 409
                    return
        else:
            cherrypy.response.status = 404
