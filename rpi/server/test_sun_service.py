from dateutil import parser
from SunService import SunSerivce


def test_winter_sunrise_date_time():
    service = SunSerivce()
    sunrise_date = service.get_sunrise(parser.parse("2015-12-27 00:00:00"))
    expected_sunrise = parser.parse("2015-12-27 07:35:03")
    assert sunrise_date == expected_sunrise


def test_correct_summer_sunrise_date_time():
    service = SunSerivce()
    sunrise_date = service.get_sunrise(parser.parse("2015-08-23 00:00:00"))
    expected_sunrise = parser.parse("2015-08-23 05:29:15")
    assert sunrise_date == expected_sunrise


def test_winter_sunset_date_time():
    service = SunSerivce()
    sunrise_date = service.get_sunset(parser.parse("2015-12-27 00:00:00"))
    expected_sunrise = parser.parse("2015-12-27 15:28:28")
    assert sunrise_date == expected_sunrise


def test_summer_sunset_date_time():
    service = SunSerivce()
    sunrise_date = service.get_sunset(parser.parse("2015-08-23 00:00:00"))
    expected_sunrise = parser.parse("2015-08-23 19:38:43")
    assert sunrise_date == expected_sunrise
