#!/usr/bin/python
# -*- coding: utf-8 -*-

from json import JSONEncoder, dumps
import datetime

class ComplexEncoder(JSONEncoder):
	def default(self, obj):
		if isinstance(obj, complex):
			return [obj.real, obj.imag]
		return JSONEncoder.default(self, obj)

class DateTimeEncoder(JSONEncoder):
	def default(self, obj):
		if isinstance(obj, datetime.datetime):
			return obj.isoformat()
		return JSONEncoder.default(self, obj)

def api_return(a, o, r=None):
	obj = {'action': a}
	if o is not None: obj['obj'] = o
	if r is not None: obj['result'] = r
	else: 
		if o is not None: 
			obj['result'] = True
	return dumps(obj, cls=DateTimeEncoder, ensure_ascii=False)
