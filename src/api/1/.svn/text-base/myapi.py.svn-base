#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import *
from utils import DateTimeEncoder, api_return
import task
import project 

ACTION_TASK_LIST   = 'task_list'
ACTION_TASK_ADD    = 'task_add'
ACTION_TASK_MODIFY = 'task_modify'
ACTION_TASK_DEL    = 'task_del'
ACTION_PROJECT_LIST   = 'project_list'
ACTION_PROJECT_ADD    = 'project_add'
ACTION_PROJECT_MODIFY = 'project_modify'
ACTION_PROJECT_DEL    = 'project_del'

app = default_app()

@get('/')
@get('/tasks/list')
@get('/tasks/list/<count:int>')
def list_tasks(count = None):
	if count <= 0: count = None
	print('list_tasks: count=%d' % count)
	tasks = task.get_tasks(count)
	return api_return(ACTION_TASK_LIST, tasks)		

@get('/task/add')
def add_task():
	content = request.GET.get('c','')
	type    = request.GET.get('tp','')
	if type == '': type=None
	print('add_task: c=%s, tp=%s' % (content, type))
	id = task.add_task({'content': content, 'type': type })
	return api_return(ACTION_TASK_ADD, id)

@get('/task/modify')
def modify_task():
	id      = request.GET.get('id','');
	content = request.GET.get('c','');
	status  = request.GET.get('s','')
	if status == '': status=None
	print('modify_task: id=%d, c=%s, s=%d' % (id, content, status))
	result = task.modify_task(id, content, status)
	return api_return(ACTION_TASK_MODIFY, id, result)

@get('/task/del/<id:int>')
def del_task(id):
	print('del_task: id=%d' % id)
	result = task.del_task(id)
	return api_return(ACTION_TASK_DEL, id, result)

@get('/projects/list')
def list_projects():
	projs = project.get_projects()
	return api_return(ACTION_PROJECT_LIST, projs)

@get('/project/add')
def add_project():
	name=request.GET.get('n','')
	desc=request.GET.get('d','')	
	if desc == '': desc=None
	print('add_project: n=%s, d=%s' % (name,desc))
	id = project.add_project({'name': name, 'status': 1, 'desc': desc})	
	return api_return(ACTION_PROJECT_ADD, id)

@get('/project/modify')
def modify_project():
	id      = request.GET.get('id','');
	name    = request.GET.get('n','');
	status  = request.GET.get('s','')
	desc    = request.GET.get('d','')
	if name == '': name =None
	if status == '': status=None
	if desc == '': desc =None
	print('modify_project: id=%s, n=%s, s=%s, d=%s' % (id, name, status, desc))
	result = project.modify_project(id, name, status, desc)
	return api_return(ACTION_PROJECT_MODIFY, id, result)

@get('/project/del/<id:int>')
def del_project(id):
	print('del_project: id=%d' % id)
	result = project.del_project(id)
	return api_return(ACTION_PROJECT_DEL, id, result)
