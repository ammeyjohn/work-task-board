#!/usr/bin/python
# -*- coding: utf-8 -*-

import setting

from libs.bottle import Bottle, debug, run

app = Bottle()

if __name__ == "__main":
	debug(True)
	run(app, host="0.0.0.0", port=8080, reloader=True)

