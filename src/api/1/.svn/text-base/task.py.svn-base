#!/usr/bin/python
# -*- coding: utf-8 -*-

import db
import time
from error import *
from utils import func_return
from datetime import datetime

''' To represent the task has deleted '''
__TASK_DELETE_FLAG = -1

__DEFALUT_TASK_TYPE   = 1
__DEFALUT_TASK_STATUS = 1

def get_tasks(dic):
	sql_str = '''SELECT id, 
						content, 
						type, 
						project_id, 
						status, 
						user_id, 
						create_time, 
						update_time,
						expect_date,
						priority,
						assign_user
				FROM tasks WHERE status <> %d''' % __TASK_DELETE_FLAG

	sql_param = []
	if dic is not None:
		if dic['id'] is not None:
			sql_str += ' AND id IN (%s)'
			sql_param.append(dic['id'])

		if dic['t'] is not None:
			sql_str += ' AND type IN (%s)'
			sql_param.append(dic['t'])

		if dic['s'] is not None:
			sql_str += ' AND status IN (%s)'
			sql_param.append(dic['s'])

		if dic['uid'] is not None:
			sql_str += ' AND user_id IN (%s)'
			sql_param.append(dic['uid'])

		if dic['pid'] is not None:
			sql_str += ' AND project_id IN (%s)'
			sql_param.append(dic['pid'])

		sql_str += ' ORDER BY id DESC'
		if dic['cnt'] is not None:
			sql_str += ' LIMIT %s' % dic['cnt'] 
	r = db.exec_query(sql_str, sql_param)	
	if not r['ret_res']: return r

	tasks = []

	try:
		rows = r['ret_val']
		for row in rows:	
			tasks.append({
				'id': row[0], 
				'content': row[1],
				'type': row[2],
				'project_id': row[3],
				'status': row[4],
				'user_id': row[5],
				'create_time': row[6],
				'update_time': row[7],
				'expect_date': row[8],
				'priority': row[9],
				'assign_user': row[10]
			})
		return func_return(True, tasks) 
	except Exception, e:
		return func_return(False, ex=e)

def add_task(dic):

	content = dic['c']
	if content is None: 
		return func_return(False, ERR_103)

	uid = dic['uid']
	if uid is None:
		return func_return(False, ERR_101) 

	pid = dic['pid']
	if pid is None:
		return func_return(False, ERR_102)

	exp = dic['exp']
	if exp is None:	
		exp = time.strftime('%Y-%m-%d',time.localtime())

	priority = dic['priority']
	if priority is None: priority = 0;	

	assign_user = dic['ass']
	if assign_user is None: 
		assign_user = uid;	

	sql_str = '''INSERT INTO tasks(content, 
								   type, 
								   project_id,
								   status, 
								   user_id,	
								   create_time, 
								   update_time,
								   expect_date,
								   priority,
								   assign_user) 
				VALUES(%s, %s, %s, %s, %s, now(), now(), %s, %s, %s)'''

	t = dic['t']
	if t is None: t = __DEFALUT_TASK_TYPE

	sql_param = (content, t, pid, __DEFALUT_TASK_STATUS, uid, exp, priority, assign_user)

	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r;

	''' If task has been added, we should get the new task id '''
	r = db.exec_query_scale('SELECT MAX(ID) FROM tasks')
	if not r['ret_res']: return r;
	return func_return(True, r['ret_val'][0]) 

def modify_task(dic):

	id = dic['id']
	if id is None:
		return func_return(False, msg=ERR_104)

	sql_str = '' 
	sql_param = []
	is_update = False

	if dic['c'] is not None:
		sql_str += ',content=%s'
		sql_param.append(dic['c'])
		is_update = True

	if dic['s'] is not None:
		sql_str += ',status=%s'
		sql_param.append(dic['s'])
		is_update = True

	if dic['t'] is not None:
		sql_str += ',type=%s'
		sql_param.append(dic['t'])
		is_update = True

	if dic['exp'] is not None:
		sql_str += ',expect_date=%s'
		sql_param.append(dic['exp'])
		is_update = True

	if dic['priority'] is not None:
		sql_str += ',priority=%s'
		sql_param.append(dic['priority'])
		is_update = True

	if dic['ass'] is not None:
		sql_str += ',assign_user=%s'
		sql_param.append(dic['ass'])
		is_update = True

	if is_update:
		sql_str = 'UPDATE tasks SET update_time=now(), ' + sql_str[1:] + ' WHERE id=%s'
		sql_param.append(id)
	else: return func_return(False, ERR_013)

	r = db.exec_command(sql_str, sql_param)		
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)

def del_task(dic):
	id = dic['id']
	if id is None:
		return func_return(False, msg=ERR_104)

	sql_str = '''UPDATE tasks SET status=%s, update_time=now() 
			   WHERE id=%s'''
	sql_param = (__TASK_DELETE_FLAG, id)
	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)
