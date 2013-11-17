#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import *
from utils import *
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

@get('/index/uid=<uid:int>/pid=<pid:int>/s=<s>')
def index(uid=0, pid=0, s=None):
	dic = api_get(request, {'a':None, 'b': None})	
	return dumps(dic)

@get('/')
@get('/tasks/list')
def list_tasks():
	req_get_dic = api_get(request, ['t','s','uid','pid','cnt'])
	result = task.get_tasks(req_get_dic)
	return api_return(ACTION_TASK_LIST, result)		

@get('/task/add')
def add_task():
	req_get_dic = api_get(request, ['c','t','uid','pid'])
	result = task.add_task(req_get_dic)
	return api_return(ACTION_TASK_ADD, result)

@get('/task/modify')
def modify_task():
	req_get_dic = api_get(request, ['id','c','t','s'])
	result = task.modify_task(req_get_dic)
	return api_return(ACTION_TASK_MODIFY, result)

@get('/task/del')
def del_task():
	req_get_dic = api_get(request, ['id'])
	result = task.del_task(req_get_dic)
	return api_return(ACTION_TASK_DEL, result)

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
