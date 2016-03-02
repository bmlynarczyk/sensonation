from application.DefaultSchedulerPolicy import DefaultSchedulerPolicy
from application.WeekendSchedulerPolicy import WeekendSchedulerPolicy
from apscheduler.schedulers.background import BackgroundScheduler

class SchedulerService(object):

    def __init__(self, blinds, arduino):
        self.blinds = blinds
        self.arduino = arduino
        self.scheduler = BackgroundScheduler()
        self.scheduler.start()
        self.set_default_policy()

    def set_default_policy(self):
        self.remove_jobs()
        DefaultSchedulerPolicy(self.blinds, self.arduino, self.scheduler)

    def set_weekend_policy(self):
        self.remove_jobs()
        WeekendSchedulerPolicy(self.blinds, self.arduino, self.scheduler)

    def remove_jobs(self):
        it = iter(self.scheduler.get_jobs())
        for i in range(len(self.scheduler.get_jobs())):
            job = it.next()
            self.scheduler.remove_job(job.id)

    def get_jobs(self):
        return self.scheduler.get_jobs()

    def set_policy(self, policy):
        if policy not in ['default', 'weekend']:
            raise ValueError("wrong policy for scheduling")
        elif policy == 'weekend':
            self.set_weekend_policy()
        else:
            self.set_default_policy()
