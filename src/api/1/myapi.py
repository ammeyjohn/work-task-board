#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import *
from utils import DateTimeEncoder, api_return
import task

ACTION_TASK_LIST = 'task_list'
ACTION_TASK_ADD  = 'task_add'
ACTION_TASK_DEL  = 'task_del'

app = default_app()

@get('/')
@get('/tasks/list')
@get('/tasks/list/<count:int>')
def list_tasks(count = None):
	if count <= 0: count = None
	tasks = task.get_tasks(count)
	return api_return(ACTION_TASK_LIST, tasks)		

@get('/task/add')
def add_task():
	content = request.GET.get('c','');
	type    = request.GET.get('t','')
	id = task.add_task({'content': content, 'type': type })
	return api_return(ACTION_TASK_ADD, id)

@get('/task/del/<id:int>')
def del_task(id):
	result = task.del_task(id)
	return api_return(ACTION_TASK_DEL, id, result)
