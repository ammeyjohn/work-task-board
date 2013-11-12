#!/usr/bin/python
# -*- coding: utf-8 -*-

from ./db import base
from sqlalchemy import Column, Integer, String, Text, DateTime, ForeignKey
from datetime import datetime

class Task(base):
	__tablename__ = 'tasks'

	id          = Column('id', Integer, primary_key=True)
	type        = Column('type', Integer, nullable=False)
	status      = Column('status', Integer, nullable=False)
	content     = Column('content', Text, nullable=False)
	create_time = Column('create_time', DateTime, nullable=False)
	update_time = Column('update_time', DateTime, nullable=False)

	def __init__(self, content, type=None):
		if type is None: type = 1
		self.conent      = content
		self.type        = type
		self.status      = 1
		self.create_time = datetime.now
		self.update_time = datetime.now
