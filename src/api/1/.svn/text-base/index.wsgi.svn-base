#!/usr/bin/python
# -*- coding: utf-8 -*-

import myapi
from libs.bottle import *

app = myapi.app

if __name__ == '__main__':
    debug(True)
    run(app, host='0.0.0.0', port=8080, reloader=True)
else:
    import sae
    debug(True)
    application = sae.create_wsgi_app(app)
