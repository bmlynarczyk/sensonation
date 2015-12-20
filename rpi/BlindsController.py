import json
from BlindView import BlindView

__author__ = 'bmlynarczyk'


class BlindsController:

    exposed = True

    def __init__(self, blinds):
        self.blinds = blinds

    def GET(self):
        views = []
        for blind in self.blinds:
            views.append(BlindView(blind.name).__dict__)
        return json.dumps(views)
