from domain.ArduinoState import ArduinoState
import datetime
import logging
from application.SunService import SunSerivce
import time


class SchedulerService(object):

    def __init__(self, blinds, arduino, scheduler):
        self.arduino = arduino
        self.sun_service = SunSerivce()
        self.scheduler = scheduler
        self.blinds = blinds
        self.init()

    def init(self):
        now = datetime.datetime.now()
        sunrise = self.sun_service.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        if now < sunrise:
            self.scheduler.add_job(self.pull_up_job, 'date', run_date=sunrise)
        if now < sunset:
            self.scheduler.add_job(self.pull_down_job, 'date', run_date=sunset)
        self.scheduler.add_job(self.recalc_job, 'date', run_date=tommorow_1_am)

    def get_tommorow_1_am(self, date):
        return date.replace(hour=1) + datetime.timedelta(days=1)

    def recalc_job(self):
        now = datetime.datetime.now()
        sunrise = self.sun_service.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        self.scheduler.add_job(self.pull_up_job, 'date', run_date=sunrise)
        self.scheduler.add_job(self.pull_down_job, 'date', run_date=sunset)
        self.scheduler.add_job(self.recalc_job, 'date', run_date=tommorow_1_am)

    def pull_down_job(self):
        self.blinds.pull_down()

    def pull_up_job(self):
        self.blinds.pull_up()
