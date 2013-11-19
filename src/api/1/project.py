#!/usr/bin/python
# -*- coding: utf-8 -*-

import db
from error import *
from utils import *
from datetime import datetime

''' To represent the task has deleted '''
__PROJ_DELETE_FLAG = -1

__DEFALUT_PROJECT_STATUS = 1

def get_projects():
	sql_str = '''SELECT id, name, status, user_id, description, create_time 
			  FROM projects WHERE status > 0 ORDER BY id'''
	r = db.exec_query(sql_str)	
	if not r['ret_res']: return r

	projs = []
	rows = r['ret_val'] 
	for row in rows:
		projs.append({
			'id': row[0], 
			'name': row[1],
			'status': row[2],
			'user_id': row[3],
			'desc': row[4],
			'create_time': row[5],
		})
	return func_return(True, projs)

def add_project(dic):

	name = dic['n']
	if name is None: 
		return func_return(False, ERR_105)

	uid = dic['uid']
	if uid is None:
		return func_return(False, ERR_101) 

	desc = dic['desc']

	sql_str = '''INSERT INTO projects(name, 
									  status, 
									  user_id,
									  description, 
									  create_time) 
				VALUES(%s, %s, %s, %s, curdate())'''
	sql_param = (name, __DEFALUT_PROJECT_STATUS, uid, desc)

	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r;

	''' If project has been added, we should get the new project id '''
	r = db.exec_query_scale('SELECT MAX(ID) FROM projects')
	if not r['ret_res']: return r;
	return func_return(True, r['ret_val'][0]) 

def modify_project(dic):
	id = dic['id']
	if id is None:
		return func_return(False, msg=ERR_104)

	sql_str = '' 
	sql_param = []
	is_update = False

	if dic['n'] is not None:
		sql_str += ',name=%s'
		sql_param.append(dic['n'])
		is_update = True

	if dic['s'] is not None:
		sql_str += ',status=%s'
		sql_param.append(dic['s'])
		is_update = True

	if dic['d'] is not None:
		sql_str += ',description=%s'
		sql_param.append(dic['d'])
		is_update = True

	if is_update:
		sql_str = 'UPDATE projects SET ' + sql_str[1:] + ' WHERE id=%s'
		sql_param.append(id)
	else: return func_return(False, ERR_013)

	r = db.exec_command(sql_str, sql_param)		
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)	

def del_project(dic):
	id = dic['id']
	if id is None:
		return func_return(False, msg=ERR_104)

	sql_str = '''UPDATE projects SET status=%s WHERE id=%s'''
	sql_param = (__PROJ_DELETE_FLAG, id)
	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)
