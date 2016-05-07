from apscheduler.schedulers.background import BackgroundScheduler
from dateutil import parser
from freezegun import freeze_time
from pytz import timezone
from application.SchedulerService import SchedulerService
import unittest

warsaw = timezone('Europe/Warsaw')

class TestSchedulerService(unittest.TestCase):

    @freeze_time("2015-12-23 00:00:00")
    def test_initialisation_when_now_is_before_sunrise(self):
        service = SchedulerService([])
        self.assertEqual(3, len(service.get_jobs()))
        it = iter(service.get_jobs())
        first_job = it.next()
        self.assertEqual(first_job.trigger.run_date, self.get_date("2015-12-23 07:33:46"))
        self.assertEqual(first_job.name, 'DefaultSchedulerPolicy.pull_up_job')
        second_job = it.next()
        self.assertEqual(second_job.trigger.run_date, self.get_date("2015-12-23 15:25:54"))
        self.assertEqual(second_job.name, 'DefaultSchedulerPolicy.pull_down_job')
        third_job = it.next()
        self.assertEqual(third_job.trigger.run_date, self.get_date("2015-12-24 01:00:00"))
        self.assertEqual(third_job.name, 'DefaultSchedulerPolicy.recalc_job')


    @freeze_time("2015-12-23 08:00:00")
    def test_initialisation_when_now_is_after_sunrise(self):
        service = SchedulerService([])
        self.assertEqual(2, len(service.get_jobs()))
        it = iter(service.get_jobs())
        first_job = it.next()
        self.assertEqual(first_job.trigger.run_date, self.get_date("2015-12-23 15:25:54"))
        self.assertEqual(first_job.name, 'DefaultSchedulerPolicy.pull_down_job')
        second_job = it.next()
        self.assertEqual(second_job.trigger.run_date, self.get_date("2015-12-24 01:00:00"))
        self.assertEqual(second_job.name, 'DefaultSchedulerPolicy.recalc_job')


    @freeze_time("2015-08-23 21:00:00")
    def test_initialisation_when_now_is_after_sunset(self):
        service = SchedulerService([])
        self.assertEqual(1, len(service.get_jobs()))
        it = iter(service.get_jobs())
        it = iter(service.get_jobs())
        first_job = it.next()
        self.assertEqual(first_job.trigger.run_date, self.get_date("2015-08-24 01:00:00"))
        self.assertEqual(first_job.name, 'DefaultSchedulerPolicy.recalc_job')

    @freeze_time("2015-08-23 04:00:00")
    def test_policy_change(self):
        service = SchedulerService([])
        self.assertEqual(3, len(service.get_jobs()))
        service.set_policy('weekend')
        self.assertEqual(2, len(service.get_jobs()))
        it = iter(service.get_jobs())
        self.assertEqual(it.next().name, 'WeekendSchedulerPolicy.pull_down_job')
        service.set_policy('default')
        self.assertEqual(3, len(service.get_jobs()))

    def get_date(self, date):
        return warsaw.localize(parser.parse(date))
