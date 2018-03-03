import csv
from dataCollector import DataCollector
from random import shuffle


data = DataCollector()

newsAgencies = ["the-economist", "bloomberg", "financial-times", "crypto-coin-news"]
				
clickbait = ["buzzfeed", "entertainment-weekly", "mirror", "mtv-news"]

def create_all_news_CSV():
	with open('BTCheadlines.csv', 'w', newline='') as csvfile:
		
		writer = csv.writer(csvfile, delimiter=';', 
								dialect = "excel-tab")
		writer.writerow(["id", "name", "author", "title", "description", "url", "urlToImage",
						"publishedAt"])
		for article in allData:
			fields = []
			try:
				if article[header[0]]['id'] == None:
					fields.append("None")
				else:
					fields.append(article[header[0]]['id'])
				
				if article[header[0]]['name'] == None:
					fields.append("None")
				else:
					fields.append(article[header[0]]['name'])
				
				fields += ([article[key] for key in list(article.keys())[1:]])
				
				for f in fields:
					if f != None:
						f = ' '.join(f.split())
					else:
						f = "None"
				
				writer.writerow(fields)
			except UnicodeEncodeError:
				continue

	print ("DONE: a CSV file with all news sources and its relevant data.")
	
def create_headline_CSV(filename, agencies):
	"""Create a csv file with headlines and their description from buzzfeed."""
	
	newsAgencies = agencies
	allData = []
	
	for agency in newsAgencies:
		
		print ("Starting to fetch: " + agency)
		allData += data.request_data(n=1000, source=agency)
		data.reset()
		print (agency + " finished")
	
	shuffle(allData)
	
	header = list(allData[0])

	print("Header: " + str(header))
	
	with open(filename, 'w', newline='') as csvfile:
		# Write the csv file with ';' and the header title data
		writer = csv.writer(csvfile, delimiter=';', dialect = "excel-tab")
		writer.writerow(["title", "description"])
		for article in allData:
			fields = []
			# If a headline contains any letter that are not decodable, ignore it.
			try:
				# Create a row
				fields = [article['title'], article['description']]
				for f in fields:
					if f != None:
						f = ' '.join(f.split())
					else:
						f = "None"
				# Write a row of data
				writer.writerow(fields)
			except UnicodeEncodeError:
				continue

	print ("DONE: a CSV file with headlines and descriptions has been created.")
	
	
	
create_headline_CSV("headlines2.csv", newsAgencies)

print("News agencies done.")
print()
print("Starting on clickbait...")

create_headline_CSV("cb_headlines2.csv", clickbait)

print("Clickbait done.")