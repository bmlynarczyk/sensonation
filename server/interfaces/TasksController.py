from interfaces.views.JobView import JobView
from application.SchedulerService import SchedulerService


class TasksController(object):

    def __init__(self, blinds, arduino):
        self.service = SchedulerService(blinds, arduino)

    def GET(self):
        views = []
        it = iter(self.service.get_jobs())
        for i in range(len(self.service.get_jobs())):
            job = it.next()
            views.append(JobView(job.id, job.name).__dict__)
        return views
