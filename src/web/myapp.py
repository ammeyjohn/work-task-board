#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import Bottle, route, static_file
from libs.bottle import debug, run
from libs.bottle import jinja2_view as view

app = Bottle()

@app.route("/")
@view("templates/hello.html")
def index():
    return { 'title': 'Simple TODO List' }

@app.route("/static/<filename:path>")
def static(filename):
    return static_file(filename, root='static/')

if __name__ == '__main__':
    debug(True)
    run(app, reloader=True, host='0.0.0.0', port=8081)
