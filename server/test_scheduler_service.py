from apscheduler.schedulers.background import BackgroundScheduler
from Arduino import Arduino
from dateutil import parser
from freezegun import freeze_time
from pytz import timezone
from SchedulerService import SchedulerService

warsaw = timezone('Europe/Warsaw')


@freeze_time("2015-08-23 00:00:00")
def test_initialisation_when_now_is_before_sunrise():
    scheduler = BackgroundScheduler()
    SchedulerService([], Arduino(), scheduler)
    assert 3 == len(scheduler.get_jobs())
    it = iter(scheduler.get_jobs())
    first_job = it.next()
    assert first_job.trigger.run_date == get_date("2015-08-23 05:29:15")
    assert first_job.name == 'SchedulerService.pull_up_job'
    second_job = it.next()
    assert second_job.trigger.run_date == get_date("2015-08-23 19:38:43")
    assert second_job.name == 'SchedulerService.pull_down_job'
    third_job = it.next()
    assert third_job.trigger.run_date == get_date("2015-08-24 01:00:00")
    assert third_job.name == 'SchedulerService.recalc_job'


@freeze_time("2015-08-23 07:00:00")
def test_initialisation_when_now_is_after_sunrise():
    scheduler = BackgroundScheduler()
    SchedulerService([], Arduino(), scheduler)
    assert 2 == len(scheduler.get_jobs())
    it = iter(scheduler.get_jobs())
    first_job = it.next()
    assert first_job.trigger.run_date == get_date("2015-08-23 19:38:43")
    assert first_job.name == 'SchedulerService.pull_down_job'
    second_job = it.next()
    assert second_job.trigger.run_date == get_date("2015-08-24 01:00:00")
    assert second_job.name == 'SchedulerService.recalc_job'


@freeze_time("2015-08-23 21:00:00")
def test_initialisation_when_now_is_after_sunset():
    scheduler = BackgroundScheduler()
    SchedulerService([], Arduino(), scheduler)
    assert 1 == len(scheduler.get_jobs())
    it = iter(scheduler.get_jobs())
    it = iter(scheduler.get_jobs())
    first_job = it.next()
    assert first_job.trigger.run_date == get_date("2015-08-24 01:00:00")
    assert first_job.name == 'SchedulerService.recalc_job'


def get_date(date):
    return warsaw.localize(parser.parse(date))
