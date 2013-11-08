#!/usr/bin/python
# -*- coding: utf-8 -*-


from json import dumps
from libs.bottle import route, run, response


@route('/')
def index():
    rv = [{"id": 1, "name": "Test Item 1"}, {"id": 2, "name": "Test Item 2"}]
    response.content_type = 'application/json'
    return dumps(rv)


if __name__ == "__main__":
    run(host='0.0.0.0', port=8080, debug=True, reloader=True)
