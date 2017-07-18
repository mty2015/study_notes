import sys, os
import argparse

if __name__ == '__main__':
    parse = argparse.ArgumentParser(argument_default=12)
    parse.add_argument('--foo', help='foo help')
    parse.add_argument('--fe', help='fe help')
    parse.add_argument('bar', nargs='+', help='bar help')
    args = parse.parse_args()
    print('======', args)
