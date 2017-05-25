import unittest
import websockettest_stable
import websockettest_error

class TestMechanic(unittest.TestCase):
    """docstring for Test"""
    def test_stable(self):
        websockettest_stable.start(self)
    def test_error(self):
        websockettest_error.start(self)

if __name__ == '__main__':
    unittest.main()
