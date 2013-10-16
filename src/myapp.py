#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import route, run, template, static_file


@route("/hello/<title>")
def index(title='World'):
    return template('./templates/base.html', title=title)


@route("/static/<filename:path>")
def static(filename):
    return static_file(filename, root='static/')


run(reloader=True, host='localhost', port=8081)
