#!/usr/bin/env python3

from faker import Factory
from websocket import create_connection
from websocket import WebSocketApp
import ssl
import requests
import time 
import json
import _thread as thread



class LobbySocket:
    def __init__(self, cookie):
        self.lobby_socket = None
        self.cookie = cookie
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
        print(message)
        pass


class GameSocket:
    def __init__(self, cookie):
        self.game_socket = None
        self.cookie = cookie
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

    def handle(self,message):
        print(message)
        tp = message["type"]
        if tp == "GetCardFromHand":
            self.game_socket.send('{"type":"ChooseCardFromHand", "chosenCard":0}')
        if tp == "GetCardFromTable":
            self.game_socket.send('{"type":"ChooseCardFromTable", "chosenCard":0}')



class User(object):
    """docstring for User"""
    def __init__(self):
        self.lobby_socket = None
        self.game_socket = None
        self.cookie = None
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
        print(rsp.text)
        rsp = session.get("http://localhost:8080/api/user/data")
        print(rsp.text)
        print(session.cookies.get_dict())
        cookies = session.cookies.get_dict()
        name, val = cookies.popitem()
        self.cookie = "{}={}".format(name,val)

    def connect_lobby(self):
       self.lobby_socket = LobbySocket(self.cookie)
       self.lobby_socket.open_lobby()

    def connect_game(self):
       self.game_socket = GameSocket(self.cookie)
       self.game_socket.open_game()


def main():
    user = User()
    user.prepare()
    user.connect_lobby()
    user.connect_game()


if __name__ == '__main__':
    main()