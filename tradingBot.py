# -*- coding: utf-8 -*-

import os
import sys
#import asciichart
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime

years = mdates.YearLocator()
months = mdates.MonthLocator()
yearsFmt = mdates.DateFormatter('%Y')

# -----------------------------------------------------------------------------

this_folder = os.path.dirname(os.path.abspath(__file__))
root_folder = os.path.dirname(os.path.dirname(this_folder))
sys.path.append(root_folder + '/python')
sys.path.append(this_folder)

# -----------------------------------------------------------------------------

import ccxt	 # noqa: E402

# -----------------------------------------------------------------------------
print (datetime.fromtimestamp(1485714600).strftime("%A, %B %d, %Y %I:%M:%S"))
exchange = ccxt.binance()
symbol = 'LINK/ETH'

# each ohlcv candle is a list of [ timestamp, open, high, low, close, volume ]
index = 4  # use close price from each ohlcv candle

length = 80
height = 15
fig, ax = plt.subplots()

def print_chart(exchange, symbol, timeframe):

	# get a list of ohlcv candles
	ohlcv = exchange.fetch_ohlcv(symbol, timeframe)

	# get the ohlCv (closing price, index == 4)
	series = [x[index] for x in ohlcv]
	t = [datetime.fromtimestamp(y[0]/1000).strftime("%B %Y") for y in ohlcv]
	date = t
	#print (ohlcv)
	
	#ax.plot(t, series)

	#ax.xaxis.set_major_locator(years)
	#ax.xaxis.set_major_formatter(yearsFmt)
	#ax.xaxis.set_minor_locator(months)
	
	#datemin = datetime.date(min(t), 1, 1)
	#datemax = datetime.date(max(t) + 1, 1, 1)
	#ax.set_xlim(datemin, datemax)

	last = ohlcv[len(ohlcv) - 1][index]	 # last closing price
	return last

# format the coords message box
def price(x):
	return '$%1.2f' % x
ax.format_xdata = mdates.DateFormatter('%Y-%m-%d')
ax.format_ydata = price
ax.grid(True)	
	
fig.autofmt_xdate()
last = print_chart(exchange, symbol, '1m')
#plt.show()
print("\n" + exchange.name + ' last price: ' + str(last) + "\n") # print last closing price