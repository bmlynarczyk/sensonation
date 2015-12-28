from ArduinoState import ArduinoState
from datetime import datetime
import logging
from SunService import SunSerivce
import time


class SchedulerService(object):

    def __init__(self, blinds, arduino, scheduler):
        self.logger = logging.getLogger("SchedulerCallback")
        self.arduino = arduino
        self.sun_service = SunSerivce()
        self.scheduler = scheduler
        self.scheduler.start()
        self.init()

    def init(self):
        now = datetime.datetime.now()
        sunrise = self.sun_service.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        if now < sunrise:
            self.scheduler.add_date_job(self.pull_up_job, sunrise, [self])
        if now < sunset:
            self.scheduler.add_date_job(self.pull_down_job, sunset, [self])
        self.scheduler.add_date_job(self.recalc_job, tommorow_1_am, [self])

    def get_tommorow_1_am(self, date):
        return date.replace(hour=1) + datetime.timedelta(day=1)

    def recalc_job(self):
        now = datetime.datetime.now()
        sunrise = self.sun_service.get_sunrise(now)
        sunset = self.sun_service.get_sunset(now)
        tommorow_1_am = self.get_tommorow_1_am(now)
        self.scheduler.add_date_job(self.pull_up_job, sunrise, [self])
        self.scheduler.add_date_job(self.pull_down_job, sunset, [self])
        self.scheduler.add_date_job(self.recalc_job, tommorow_1_am, [self])

    def pull_down_job(self):
        for blind in self.blinds:
            blind.pullDown()
            while self.arduino.state == ArduinoState.STOP_ONLY:
                time.sleep(1)
        self.recalc_job()

    def pull_up_job(self):
        for blind in self.blinds:
            blind.pullUp()
            while self.arduino.state == ArduinoState.STOP_ONLY:
                time.sleep(1)
