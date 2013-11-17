#!/usr/bin/python
# -*- coding: utf-8 -*-

from libs.bottle import *
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


def api_get(req, keys, empty=True):
    if req is None or keys is None:
        return None
    dic = {}
    for k in keys:
        v = req.GET.get(k, '')
        if v == '' and empty:
            v = None
        if not dic.has_key(k):
            dic[k] = v
    return dic


def func_return(res, val=None, msg=None, ex=None):
    return {'ret_res': res,
            'ret_val': val,
            'ret_msg': msg,
            'ret_ex': ex}


def api_return(a, o):
    obj = {'action': a, 'obj': None}
    if o is not None: 
        obj['obj'] = o['ret_val'] 
        obj['result'] = o['ret_res']
        if o['ret_msg'] is not None: obj['msg'] = o['ret_msg']
        if o['ret_ex'] is not None: obj['err'] = o['ret_ex'].__str__()
    return dumps(obj, cls=DateTimeEncoder, ensure_ascii=False)
