#!/usr/bin/python
# -*- coding: utf-8 -*-

from error import ERR_011
from utils import func_return
from sae import const
import traceback
import MySQLdb

MYSQL_DB     = const.MYSQL_DB
MYSQL_USER   = const.MYSQL_USER
MYSQL_PASS   = const.MYSQL_PASS 
MYSQL_HOST_M = const.MYSQL_HOST
MYSQL_PORT   = int(const.MYSQL_PORT)

__conn   = None
__cursor = None

def __create_mysql_connection():
	global __conn, __cursor
	__close_mysql_connection()
	__conn   = MySQLdb.connect(host=MYSQL_HOST_M,port=MYSQL_PORT,user=MYSQL_USER,passwd=MYSQL_PASS,db=MYSQL_DB)
	__cursor = __conn.cursor()

def __close_mysql_connection():
	global __conn, __cursor
	if __cursor is not None:
		__cursor.close()
		__cursor = None;

	if __conn is not None:
	   __conn.close()
	   __conn = None

def exec_query(sql, params=None, count=None):
	__print_debug_sql(sql)
	try:
		global __conn, __cursor
		__create_mysql_connection()
		__cursor.execute(sql, params)
		if count is None or count <= 0:
			result = __cursor.fetchall()
		else:
			result = __cursor.fetchmany(count)
		return func_return(True, result)
	except BaseException, e:
		traceback.print_exc()
		return func_return(False, msg=ERR_011, ex=e)
	finally: 
		__close_mysql_connection()

def exec_query_scale(sql, params=None):
	__print_debug_sql(sql, params)
	try:
		global __conn, __cursor       
		__create_mysql_connection()        
		__cursor.execute(sql)
		result = __cursor.fetchone()	
		return func_return(True, result)
	except BaseException, e:
		traceback.print_exc()
		return func_return(False, msg=ERR_011, ex=e)
	finally:
		__close_mysql_connection()

def exec_command(sql, params=None):
	__print_debug_sql(sql, params)
	try:
		global __conn, __cursor
		__create_mysql_connection()
        
		if params is None:           
			result = __cursor.execute(sql)	
		else:            
			result = __cursor.execute(sql, params)	

		return func_return(True, result)
	except BaseException, execute:
		traceback.print_exc()
		return func_return(False, msg=ERR_011, ex=e)
	finally:
		__close_mysql_connection()

def __print_debug_sql(sql, params=None):
	print '[DEBUG] ' + sql
	if params is not None:
		i = 0
		for p in params:
			print '[DEBUG] param[%d]=' % i + str(p)
			i += 1
