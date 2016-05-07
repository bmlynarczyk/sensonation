from application.SchedulerPolicy import SchedulerPolicy
from application.SunService import SunSerivce
import datetime
import time


class WeekendSchedulerPolicy(SchedulerPolicy):

    def __init__(self, blinds, scheduler):
        self.blinds = blinds
        self.scheduler = scheduler
        self.sun_service = SunSerivce()
        self.init()

    def init(self):
        now = datetime.datetime.now()
        sunrise = self.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        if now < sunrise and now.weekday() not in [5, 6]:
            self.scheduler.add_job(self.pull_up_job, 'date', run_date=sunrise)
        if now < sunset:
            self.scheduler.add_job(self.pull_down_job, 'date', run_date=sunset)
        self.scheduler.add_job(self.recalc_job, 'date', run_date=tommorow_1_am)

    def get_tommorow_1_am(self, date):
        return date.replace(hour=1) + datetime.timedelta(days=1)

    def recalc_job(self):
        now = datetime.datetime.now()
        sunrise = self.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        if now.weekday() not in [5, 6]:
            self.scheduler.add_job(self.pull_up_job, 'date', run_date=sunrise)
        self.scheduler.add_job(self.pull_down_job, 'date', run_date=sunset)
        self.scheduler.add_job(self.recalc_job, 'date', run_date=tommorow_1_am)

    def get_sunrise(self, now):
        sunrise = self.sun_service.get_sunrise(now)
        today_6_45_am = datetime.datetime.now().replace(hour = 6, minute = 45)
        if sunrise < today_6_45_am:
            sunrise = today_6_45_am
        return sunrise

    def pull_down_job(self):
        self.blinds.pull_down()

    def pull_up_job(self):
        self.blinds.pull_up()
