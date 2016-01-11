from interfaces.views.BlindView import BlindView
import cherrypy
import json

class BlindsController(object):

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds

    def GET(self):
        views = []
        for blind in self.blinds.list:
            views.append(BlindView(blind.name).__dict__)
        return json.dumps(views)

    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def PUT(self):
        input_json = cherrypy.request.json
        action_name = input_json['action_name']
        self.blinds.fire_action(action_name)

    def OPTIONS(self):
        cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
        cherrypy.response.headers["Access-Control-Allow-Methods"] = "PUT,GET"
        cherrypy.response.headers["Access-Control-Allow-Headers"] = "Content-Type, Accept"
