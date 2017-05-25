#!/usr/bin/env python3

from faker import Factory
from websocket import create_connection
from websocket import WebSocketApp
import ssl
import requests
import time 
import json
import _thread as thread
import threading



class LobbySocket:
    def __init__(self, cookie,testsystem):
        self.lobby_socket = None
        self.cookie = cookie
        self.testsystem = testsystem
        pass


    @staticmethod
    def on_open(ws):
        def run(*args):
            while(True):
                pass
        thread.start_new_thread(run, ())

    def open_lobby(self):
        def on_recive(ws,message):
            message = json.loads(message)
            self.handle(message)
        pass
        self.lobby_socket = WebSocketApp("ws://localhost:8080/lobby",cookie=self.cookie, on_message=on_recive)
        self.lobby_socket.on_open = self.on_open
        self.lobby_socket.run_forever()

    def handle(self, message):
        #print(message)
        pass


class GameSocket:
    def __init__(self, cookie, testsystem):
        self.game_socket = None
        self.cookie = cookie
        self.testsystem = testsystem
        self.errorst = False
        self.error_next = False
        self.good_msg = ''
        pass
    @staticmethod
    def on_open(ws):
        def run(*args):
            while(True):
                pass
        thread.start_new_thread(run, ())

    def open_game(self):
        def on_recive(ws,message):
            message = json.loads(message)
            self.handle(message)
        pass
        self.game_socket = WebSocketApp("ws://localhost:8080/game",cookie=self.cookie, on_message=on_recive)
        self.game_socket.on_open = self.on_open
        self.game_socket.run_forever()

    def truehandl(self,message):
        tp = message["type"]
        if tp == "ErrorMsg":
            #print("error handled")
            self.game_socket.send(self.good_msg)
            self.error_next = False
            self.errorst = False
        if self.error_next:
            self.testsystem.fail("error was not")

    def fakehandl(self,message):
        tp = message["type"]
        if tp == "GetCardFromHand":
            self.game_socket.send('#####ERROR#####')
            self.good_msg = '{"type":"ChooseCardFromHand", "chosenCard":0}'
            #print("error send")
            self.errorst = True
            self.error_next = True
        if tp == "GetCardFromTable":
            self.game_socket.send('#####ERROR#####')
            self.good_msg = '{"type":"ChooseCardFromTable", "chosenCard":0}'
            #print("error send")
            self.errorst = True
            self.error_next = True

    def handle(self,message):
        #print(message)
        #print(self.errorst)
        if self.errorst:
            self.truehandl(message)
        else:
            self.fakehandl(message)



class User(object):
    """docstring for User"""
    def __init__(self, tsystem):
        self.lobby_socket = None
        self.game_socket = None
        self.cookie = None
        self.tsystem = tsystem
        pass

    def prepare(self):
        pss = "1111"
        fk = Factory.create()
        session = requests.Session()
        login = {
            "userMail": fk.email(),
            "userLogin": fk.user_name(),
            "pass": pss
        }
        headers = '''Accept: */*
        Connection: keep-alive
        Accept-Encoding: gzip, deflate
        User-Agent: python-requests/2.9.1
        '''
        rsp = session.post("http://localhost:8080/api/user/signup",json=login)
        #print(rsp.text)
        rsp = session.get("http://localhost:8080/api/user/data")
        #print(rsp.text)
        #print(session.cookies.get_dict())
        cookies = session.cookies.get_dict()
        name, val = cookies.popitem()
        self.cookie = "{}={}".format(name,val)

    def connect_lobby(self):
       self.lobby_socket = LobbySocket(self.cookie, self.tsystem)
       self.lobby_socket.open_lobby()

    def connect_game(self):
       self.game_socket = GameSocket(self.cookie, self.tsystem)
       self.game_socket.open_game()


def test_this(tsystem):
    user = User(tsystem)
    user.prepare()
    user.connect_lobby()
    user.connect_game()


def start(tsystem):
     threads = [threading.Thread(target=test_this,args=(tsystem,)) for i in range(0,4)]
     for i in threads:
        i.start()
     for i in threads:
        i.join()