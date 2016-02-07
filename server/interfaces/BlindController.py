from interfaces.views.BlindView import BlindView
import cherrypy
import json
import logging


class BlindController(object):

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds
        self.logger = logging.getLogger("BlindController")

    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def PUT(self):
        cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
        input_json = cherrypy.request.json
        name = input_json['name']
        action_name = input_json['action_name']
        for blind in self.blinds.list:
            if blind.name == name:
                try:
                    blind.fire_action(action_name)
                    cherrypy.response.status = 200
                    return json.dumps(BlindView(blind.name, blind.active).__dict__)
                except ValueError:
                    cherrypy.response.status = 409
                    return
        else:
            cherrypy.response.status = 404

    def OPTIONS(self):
        cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
        cherrypy.response.headers["Access-Control-Allow-Methods"] = "PUT"
        cherrypy.response.headers["Access-Control-Allow-Headers"] = "Content-Type, Accept"
