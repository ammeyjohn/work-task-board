#!/usr/bin/python
# -*- coding: utf-8 -*-

import db
from datetime import datetime

''' To represent the task has deleted '''
__TASK_DELETE_FLAG = -1

def get_tasks(count=None):
	sql_str = 'SELECT * FROM tasks WHERE status > 0 ORDER BY id DESC'
	if count is not None:
		sql_str += ' LIMIT %d' % count
	results = db.exec_query(sql_str)	

	tasks = None
	if results is not None:
		tasks = []
		for row in results:	
			tasks.append({
				'id': row[0], 
				'content': row[1],
				'type': row[2],
				'status': row[3],
				'create_time': row[4],
				'update_time': row[5]
			})
	return tasks

def add_task(task):
	if task is None: return None
	sql_str = 'INSERT INTO tasks(content, type, status, create_time, update_time) VALUES(%s, %s, %s, curdate(), curdate())'
	sql_param = (task['content'], task['type'], 1)

	result = db.exec_command(sql_str, sql_param)
	if result == False: return False

	''' If task has been added, we should get the new task id '''
	id = db.exec_query_scale('SELECT MAX(ID) FROM tasks')
	if id is not None: return id[0]
	return None

def modify_task(id, content=None, status=None):
	sql_str = '' 
	sql_param = []
	if content is not None:
		sql_str += ',content=%s'
		sql_param.append(content)
	if status is not None:
		sql_str += ',status=%s'
		sql_param.append(status)
	if sql_str != '':
		sql_str = 'UPDATE tasks SET ' + sql_str[1:] + ' WHERE id=%s'
		sql_param.append(id)
	return db.exec_command(sql_str, sql_param)		

def del_task(id):
	sql_str = 'UPDATE tasks SET status = %s WHERE id=%s'
	sql_param = (__TASK_DELETE_FLAG, id)
	return db.exec_command(sql_str, sql_param)
