#!/usr/bin/python
# -*- coding: utf-8 -*-

import json
import datetime

class ComplexEncoder(json.JSONEncoder):
	def default(self, obj):
		if isinstance(obj, complex):
			return [obj.real, obj.imag]
		return json.JSONEncoder.default(self, obj)

class DateTimeEncoder(json.JSONEncoder):
	def default(self, obj):
		if isinstance(obj, datetime.datetime):
			return obj.isoformat()
		return json.JSONEncoder.default(self, obj)
