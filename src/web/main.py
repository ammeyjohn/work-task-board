#!/usr/bin/python
# -*- coding: utf-8 -*-


#import myapp
import myapi
from bottle import debug, run
#from google.appengine.ext.webapp.util import run_wsgi_app


def main():
    debug(True)
    #run(app=myapp.app, server='gae')
    run(app=myapi.app, server='gae')
    #run_wsgi_app(app)


if __name__ == "__main__":
    main()
