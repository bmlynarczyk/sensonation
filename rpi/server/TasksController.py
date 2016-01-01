from apscheduler.schedulers.background import BackgroundScheduler
from JobView import JobView
import json
from SchedulerService import SchedulerService


class TasksController(object):

    exposed = True

    def __init__(self, blinds, arduino):
        self.scheduler = BackgroundScheduler()
        SchedulerService(blinds, arduino, self.scheduler)
        self.scheduler.start()

    def GET(self):
        views = []
        for job in self.scheduler.get_jobs:
            views.append(JobView(job.id, job.name).__dict__)
        return json.dumps(views)
