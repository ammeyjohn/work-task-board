#!/usr/bin/python
# -*- coding: utf-8 -*-

import db

def get_tasks(count=None):
	sql_str = 'SELECT * FROM tasks ORDER BY id DESC'
	if count is not None:
		sql_str += ' LIMIT %d' % count
	results = db.exec_query(sql_str)	

	tasks = []
	for row in results:	
		print row
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
	sql_str = 'INSERT INTO tasks(content) VALUES(%s)'
	sql_param = (task['content'])
	db.exec_command(sql_str, sql_param)

def del_task(id):
	sql_str = 'DELETE FROM tasks WHERE id=%s'
	sql_param = (id)
	db.exec_command(sql_str, sql_param)
	return get_tasks()
	