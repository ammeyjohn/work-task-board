#!/usr/bin/python
# -*- coding: utf-8 -*-

import db
from error import *
from utils import func_return
from datetime import datetime

''' To represent the user has deleted '''
__USER_DELETE_FLAG = -1
__DEFALUT_USER_STATUS = 1
__DEFALUT_USER_GROUP = 'DEFAULT'
__MIN_PASSWORD_LENGTH = 4

def get_users(dic):
	sql_str = '''SELECT id, 
						name, 
						password, 
						group_name,
						status, 
						create_time 
				FROM users WHERE status <> %d''' % __USER_DELETE_FLAG

	sql_param = []
	if dic is not None:
		if dic['id'] is not None:
			sql_str += ' AND id IN (%s)'
			sql_param.append(dic['id'])

		if dic['n'] is not None:
			sql_str += ' AND name IN (%s)'
			sql_param.append(dic['n'])

		if dic['s'] is not None:
			sql_str += ' AND status IN (%s)'
			sql_param.append(dic['s'])

		if dic['g'] is not None:
			sql_str += ' AND group_name IN (%s)'
			sql_param.append(dic['g'])

		sql_str += ' ORDER BY id'

	r = db.exec_query(sql_str, sql_param)	
	if not r['ret_res']: return r

	users = []

	try:
		rows = r['ret_val']
		for row in rows:	
			users.append({
				'id': row[0], 
				'name': row[1],
				'password': row[2],
				'group_name': row[3],
				'status': row[4],
				'create_time': row[5],
			})
		return func_return(True, users) 
	except Exception, e:
		return func_return(False, ex=e)

def add_user(dic):

	name = dic['n']
	if name is None: 
		return func_return(False, ERR_103)

	password = dic['pwd']
	if password is None:
		return func_return(False, ERR_106) 
	if len(password) < __MIN_PASSWORD_LENGTH:
		return func_return(False, ERR_107) 

	group = dic['g']
	if group is None:
		group = __DEFALUT_USER_GROUP

	r = get_users({'id':None,'n':name,'s':None,'g':None})
	if not r['ret_res']: return r
	if len(r['ret_val']) > 0:
		return func_return(False, ERR_108 % name)

	sql_str = '''INSERT INTO users(name, 
								   password, 
								   group_name,
								   status, 
								   create_time)
				VALUES(%s, %s, %s, %s, now())'''

	sql_param = (name, password, group, __DEFALUT_USER_STATUS) 

	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r;

	''' If user has been added, we should get the new user id '''
	r = db.exec_query_scale('SELECT MAX(ID) FROM users')
	if not r['ret_res']: return r;
	return func_return(True, r['ret_val'][0]) 

def modify_user(dic):

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

	if dic['pwd'] is not None:
		sql_str += ',password=%s'
		sql_param.append(dic['pwd'])
		is_update = True

	if dic['g'] is not None:
		sql_str += ',group_name=%s'
		sql_param.append(dic['g'])
		is_update = True

	if is_update:
		sql_str = 'UPDATE users SET ' + sql_str[1:] + ' WHERE id=%s'
		sql_param.append(id)
	else: return func_return(False, ERR_013)

	r = db.exec_command(sql_str, sql_param)		
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)

def del_user(dic):
	id = dic['id']
	if id is None:
		return func_return(False, msg=ERR_104)

	sql_str = '''UPDATE users SET status=%s WHERE id=%s'''
	sql_param = (__USER_DELETE_FLAG, id)
	r = db.exec_command(sql_str, sql_param)
	if not r['ret_res']: return r
	if r['ret_val'] is not None:
		if r['ret_val'] == 0:
			return func_return(False, msg=ERR_012)
	return func_return(True, id)
