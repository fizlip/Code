from newsapi import NewsApiClient

class DataCollector:
	
	def __init__(self):
		# Personal API key obtained from https://newsapi.org/account
		self.apiKey = 'a48fdca0e25c48689587367354692318'
		
		# Initialize variables for news-data
		self.pageNum = 1
		self.data = {}
		self.searchTerm = 'bitcoin'
		self.totAmount = 1000
		self.articles = {}							
		self.amount = 1
		self.pageNum = 0
		self.totNewsBTC = 79267
		self.page_size = 100
		self.source = "the-new-york-times"

	def pprint(self, article, name):
		"""Nice formatting for a string"""
		print (name.upper() + ": ")
		print ("	" + str(article[name.lower()]))
	
	def reset(self):
		self.amount = 0
		self.pageNum = 0
	
	def fetch_data(self, s):
		"""Get data from the news API. This is done with the newsapi lib."""
		
		self.amount = 1
		self.pageNum += 1
		if s != None:
			self.source = s
		
		newsapi = NewsApiClient(api_key=self.apiKey)
		# This method filters through the data by looking for a searchTerm given by 
		# the user and then sorting the result by a termporal axis.
		self.data = (newsapi.get_everything(sources=self.source,
										sort_by='publishedAt',
										language='en',
										page_size=self.page_size,
										page=self.pageNum))
		
		# Update collector attributes
		
		if self.data["status"] != "error":
			self.articles = self.data['articles']
			self.totAmount = self.data['totalResults']
			
			print("Total: " + str(self.totAmount))
	
	def get_data_by_type(self, source):
		"""Gets the data requested by a certain type."""
		if source != None:
			self.fetch_data(source)
		else:
			self.fetch_data()
	
	def request_data(self, source=None, n=100):
		"""Request n headlines and put them in a list."""
		
		allData = []
		
		while len(allData) < n:
			self.get_data_by_type(source)
			# If an error occurs return the data that has been fetched
			if self.data["status"] == "error":
				return allData
			
			if(len(allData) + 100 > int(self.totAmount)):
				return allData
			allData += self.articles
			print ("Amount fetched: " + str(len(allData)))
		return allData		
		
	
	def processData(self, source=None):
		"""Get data from the API. The data given has been filtered using the
		get_data method. This method prints out the headline of the every article
		and the URL from which it originates."""
		if searchterm:
			self.searchTerm = input("Search-term: ")
		
		# Get data
		self.get_data_by_type(source)
		
		while True:
			# Print one page at a time
			if self.amount <= self.page_size:
				more = input("More? (y/n)") # Ask user if more or headlines or no.
				if more == 'y':
					for article in self.articles:
						# Print headline and URL for every article on the current page
						self.pprint(article, 'Title')
						self.pprint(article, 'URL')
						#End of article
						print ("-"*50)
						self.amount += 1
						continue
				else:
					break
			else:
				# Get updated data
				self.get_data_by_type(searchterm)
				