from dateutil import parser
import suncalc


class SunSerivce(object):

    latitude = 51.25
    longitude = 22.5667

    def get_times(self, date):
        return suncalc.getTimes(
            date.replace(hour=1),
            self.latitude,
            self.longitude
        )

    def get_sunrise(self, date):
        return parser.parse(self.get_times(date).get('sunrise'))

    def get_sunset(self, date):
        return parser.parse(self.get_times(date).get('sunset'))
