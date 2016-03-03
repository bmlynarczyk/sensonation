from interfaces.views.BlindView import BlindView

class BlindsController(object):

    def __init__(self, blinds):
        self.blinds = blinds

    def GET(self):
        views = []
        for blind in self.blinds.list:
            views.append(BlindView(blind.name, blind.active).__dict__)
        return views

    def PUT(self, cherrypy):
        input_json = cherrypy.request.json
        try:
            action_name = input_json['action_name']
        except KeyError:
            cherrypy.response.status = 400
            return
        self.blinds.fire_action(action_name)
