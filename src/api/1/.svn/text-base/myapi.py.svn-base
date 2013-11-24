#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import *
from utils import *
import task
import project 
import user

ACTION_TASK_LIST   = 'task_list'
ACTION_TASK_ADD    = 'task_add'
ACTION_TASK_MODIFY = 'task_modify'
ACTION_TASK_DEL    = 'task_del'

app = default_app()

@get('/index/uid=<uid:int>/pid=<pid:int>/s=<s>')
def index(uid=0, pid=0, s=None):
	dic = api_get(request, {'a':None, 'b': None})	
	return dumps(dic)

@get('/')
@get('/tasks/list')
def list_tasks():
	req_get_dic = api_get(request, ['id','t','s','uid','pid','cnt'])
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

ACTION_PROJECT_LIST   = 'project_list'
ACTION_PROJECT_ADD    = 'project_add'
ACTION_PROJECT_MODIFY = 'project_modify'
ACTION_PROJECT_DEL    = 'project_del'

@get('/projects/list')
def list_projects():
	projs = project.get_projects()
	return api_return(ACTION_PROJECT_LIST, projs)

@get('/project/add')
def add_project():
	req_get_dic = api_get(request, ['n','uid','desc'])
	result = project.add_project(req_get_dic)	
	return api_return(ACTION_PROJECT_ADD, result)

@get('/project/modify')
def modify_project():
	req_get_dic = api_get(request, ['id','n','s','desc'])
	result = project.modify_project(req_get_dic)
	return api_return(ACTION_PROJECT_MODIFY, result)

@get('/project/del')
def del_project():
	req_get_dic = api_get(request, ['id'])
	#result = task.del_task(req_get_dic);
	result = project.del_project(req_get_dic)
	return api_return(ACTION_PROJECT_DEL, result)

ACTION_USER_LIST   = 'user_list'
ACTION_USER_ADD    = 'user_add'
ACTION_USER_MODIFY = 'user_modify'
ACTION_USER_DEL    = 'user_del'

@get('/users/list')
def list_users():
	req_get_dic = api_get(request, ['id','n','s','g'])
	users = user.get_users(req_get_dic)
	return api_return(ACTION_USER_LIST, users)

@get('/user/add')
def add_user():
	req_get_dic = api_get(request, ['n','pwd','g'])
	result = user.add_user(req_get_dic)	
	return api_return(ACTION_USER_ADD, result)

@get('/user/modify')
def modify_user():
	req_get_dic = api_get(request, ['id','n','s','pwd','g'])
	result = user.modify_user(req_get_dic)
	return api_return(ACTION_USER_MODIFY, result)

@get('/user/del')
def del_user():
	req_get_dic = api_get(request, ['id'])
	result = user.del_user(req_get_dic)
	return api_return(ACTION_USER_DEL, result)