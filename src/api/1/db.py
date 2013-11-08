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

def exec_query(sql, count=None):
	try:
		global __conn, __cursor
		__create_mysql_connection()
		__cursor.execute('SET NAMES utf8')
		__cursor.execute(sql)
		print sql
		if count is None or count <= 0:
			results = __cursor.fetchall()
		else:
			results = __cursor.fetchmany(count)
		return results
	except:
		traceback.print_exc()
		return None
	finally: 
		__close_mysql_connection()

def exec_command(sql, params=None):
	try:
		global __conn, __cursor
		__create_mysql_connection()
		__cursor.execute(sql, params);	
		print sql
	except:
		traceback.print_exc()
		return False
	finally:
		__close_mysql_connection()