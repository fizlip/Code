

getwd()

setwd("C:/Users/floppi/desktop/newsAPI")

btcHeadlines <- read.csv(file="BTCheadlines.csv", header=TRUE, sep=";", quote="")



btcHeadlines[1,]
nrow(btcHeadlines)

head(btcHeadlines$publishedAt)

header


