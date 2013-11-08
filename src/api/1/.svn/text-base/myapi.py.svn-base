#!/usr/bin/python
# -*- coding: utf-8 -*-

from json import dumps
from libs.bottle import *
import task

app = default_app()

@get('/task/add')
def add_task():
	content=request.GET.get('c','');
	task.add_task({'content': content})

@get('/tasks/list/<count:int>')
def list_tasks(count):
	if count <= 0: count = None
	tasks = task.get_tasks(count)
	return dumps(tasks);