class SchedulerPolicy:

    def init(self):
        raise NotImplementedError('SchedulerPolicy is supposed to be an abstract class!')

    def recalc_job(self):
        raise NotImplementedError('SchedulerPolicy is supposed to be an abstract class!')
