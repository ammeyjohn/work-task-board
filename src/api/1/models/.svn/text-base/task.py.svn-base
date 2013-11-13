#!/usr/bin/python
# -*- coding: utf-8 -*-

from json import dumps
from datetime import datetime
from utils import DateTimeEncoder

class Task(base):

	def __init__(self, content, type=None):
		if type is None: type = 1
		self.conent      = content
		self.type        = type
		self.status      = 1
		self.create_time = datetime.now
		self.update_time = datetime.now

	def __repr__(self):
		dic                = {}
		dic['id']          = self.id	
		dic['type']        = self.type 
		dic['status']      = self.status 
		dic['content']     = self.content 
		dic['create_time'] = self.create_time 
		dic['update_time'] = self.update_time 
		return dic
