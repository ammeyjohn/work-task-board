#!/usr/bin/python
# -*- coding: utf-8 -*-

import db

def get_tasks(count=None):
	sql_str = 'SELECT * FROM tasks ORDER BY id DESC'
	results = db.exec_query(sql_str, count)

	tasks = []
	for row in results:
		tasks.append({'id': row[0], 'content': row[1]})

	return tasks

def add_task(task):
	if task is None: return None
	sql_str = 'INSERT INTO tasks(content) VALUES(%s)'
	sql_param = (task['content'])
	db.exec_command(sql_str, sql_param)
	