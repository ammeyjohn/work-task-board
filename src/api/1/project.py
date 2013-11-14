#!/usr/bin/python
# -*- coding: utf-8 -*-

import db
from datetime import datetime

''' To represent the task has deleted '''
__PROJ_DELETE_FLAG = -1

def get_projects():
	sql_str = 'SELECT * FROM projects WHERE status > 0 ORDER BY id'
	results = db.exec_query(sql_str)	

	projs = None
	if results is not None:
		projs = []
		for row in results:	
			projs.append({
				'id': row[0], 
				'name': row[1],
				'status': row[2],
				'desc': row[3],
				'create_time': row[4],
			})
	return projs 

def add_project(proj):
	if proj is None: return None
	sql_str = 'INSERT INTO projects(name, status, description, create_time) VALUES(%s, %s, %s, curdate())'
	sql_param = (proj['name'], proj['status'], proj['desc'])

	result = db.exec_command(sql_str, sql_param)
	if result == False: return False

	''' If project has been added, we should get the new project id '''
	id = db.exec_query_scale('SELECT MAX(ID) FROM projects')
	if id is not None: return id[0]
	return None

def modify_project(id, name, status, desc):
	sql_str = 'UPDATE projects SET description=%s' 
	sql_param = [desc]
	if name is not None:
		sql_str += ',name=%s'
		sql_param.append(name)
	if status is not None:
		sql_str += ',status=%s'
		sql_param.append(status)

	sql_str += ' WHERE id=%s'
	sql_param.append(id)
	return db.exec_command(sql_str, sql_param)		

def del_project(id):
	sql_str = 'UPDATE projects SET status = %s WHERE id=%s'
	sql_param = (__PROJ_DELETE_FLAG, id)
	return db.exec_command(sql_str, sql_param)
