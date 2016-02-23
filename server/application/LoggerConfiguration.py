class LoggerConfiguration(object):
    VALUE = {
        'version': 1,

        'formatters': {
            'void': {
                'format': ''
            },
            'standard': {
                'format': '%(asctime)s [%(levelname)s] %(name)s: %(message)s'
            },
        },
        'handlers': {
            'default': {
                'level':'INFO',
                'class':'logging.StreamHandler',
                'formatter': 'standard',
                'stream': 'ext://sys.stdout'
            },
            'cherrypy_access': {
                'level':'INFO',
                'class': 'logging.handlers.RotatingFileHandler',
                'formatter': 'void',
                'filename': '/var/log/sensonation/access.log',
                'maxBytes': 10485760,
                'backupCount': 20,
                'encoding': 'utf8'
            },
            'cherrypy_error': {
                'level':'INFO',
                'class': 'logging.handlers.RotatingFileHandler',
                'formatter': 'standard',
                'filename': '/var/log/sensonation/errors.log',
                'maxBytes': 10485760,
                'backupCount': 20,
                'encoding': 'utf8'
            },
        },
        'loggers': {
            '': {
                'handlers': ['cherrypy_error'],
                'level': 'DEBUG'
            },
            'cherrypy.access': {
                'handlers': ['cherrypy_access'],
                'level': 'INFO',
                'propagate': False
            },
            'cherrypy.error': {
                'handlers': ['cherrypy_error'],
                'level': 'INFO',
                'propagate': False
            },
        }
    }
