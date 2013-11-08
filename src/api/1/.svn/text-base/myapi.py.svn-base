#!/usr/bin/python
# -*- coding: utf-8 -*-

from json import dumps
from libs.bottle import *
from utils import DateTimeEncoder
import task

app = default_app()

@route('/')
@route('/tasks/list')
@route('/tasks/list/<count:int>')
def list_tasks(count = None):
	if count <= 0: count = None
	tasks = task.get_tasks(count)
	return dumps(tasks, cls=DateTimeEncoder, ensure_ascii=False)

@route('/task/add')
def add_task():
	content=request.GET.get('c','');
	task.add_task({'content': content})

@route('/task/del/<id:int>')
def del_task(id):
	task.del_task(id)
	return list_tasks()
