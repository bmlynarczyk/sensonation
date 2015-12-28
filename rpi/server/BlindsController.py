from BlindView import BlindView
import json


class BlindsController(object):

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds

    def GET(self):
        views = []
        for blind in self.blinds:
            views.append(BlindView(blind.name).__dict__)
        return json.dumps(views)
