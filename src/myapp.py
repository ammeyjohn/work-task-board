#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import route, run, template, static_file


@route("/hello/<name>")
def index(name='World'):
    return template('./templates/hello.html', name=name)


@route("/static/<filename:path>")
def static(filename):
    return static_file(filename, root='static/')


run(reloader=True, host='localhost', port=8080)
