from interfaces.BlindController import BlindController
from interfaces.BlindsController import BlindsController
from interfaces.TasksController import TasksController
import cherrypy

class Dispatcher:

    def __init__(self, arduino, blinds):
        blindController = BlindController(blinds)
        blindsController = BlindsController(blinds)
        taskController = TasksController(blinds, arduino)
        self.context = {
            'blind': {
                'OPTIONS': lambda cherrypy: self.add_options_to_response(),
                'PUT': lambda cherrypy: blindController.PUT(cherrypy)
            },
            'blinds': {
                'GET': lambda cherrypy: blindsController.GET(),
                'OPTIONS': lambda cherrypy: self.add_options_to_response(),
                'PUT': lambda cherrypy: blindsController.PUT(cherrypy)
            },
            'tasks': {
                'GET': lambda cherrypy: taskController.GET(),
                'OPTIONS': lambda cherrypy: self.add_options_to_response()
            },
            'tasks/policy': {
                'OPTIONS': lambda cherrypy: self.add_options_to_response(),
                'PUT': lambda cherrypy: taskController.PUT(cherrypy)
            }
        }


    @cherrypy.expose
    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def api(self,*args,**kwargs):
        try:
            controller = self.context['/'.join(args)]
        except KeyError:
            cherrypy.response.status = 404
            return
        try:
            method = controller[cherrypy.request.method]
            cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
            return method(cherrypy)
        except KeyError:
            cherrypy.response.status = 405

    def add_options_to_response(self):
        cherrypy.response.headers["Access-Control-Allow-Origin"] = "*"
        cherrypy.response.headers["Access-Control-Allow-Methods"] = "PUT,GET"
        cherrypy.response.headers["Access-Control-Allow-Headers"] = "Content-Type, Accept"
