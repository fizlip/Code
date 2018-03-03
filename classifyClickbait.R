library(tm)
library(ggplot2)

getwd()

setwd("D:/newsAPI")

#
cbHeadlines <- read.csv(file="cb_headlines.csv", header=TRUE, sep=";", quote="")
headlines <- read.csv(file="headlines.csv", header=TRUE, sep=";", quote="")


head(cbHeadlines$title)
head(headlines$title)

# Get the term document matrix
get.tdm <- function(doc.vec)
{
  doc.corpus <- Corpus(VectorSource(doc.vec))
  control <- list(stopwords = TRUE, 
                  removePunctuation = TRUE, 
                  removeNumbers = TRUE, 
                  minDocFreq = 2)
  doc.dtm <- TermDocumentMatrix(doc.corpus, control)
  return (doc.dtm)
}

cbHeadlines <- cbHeadlines$title

cbHeadlines.matrix <- as.matrix(get.tdm(cbHeadlines))
cbHeadlines.counts <- rowSums(cbHeadlines.matrix)
cbHeadlines.df <- data.frame(cbind(names(cbHeadlines.counts),
                                 as.numeric(cbHeadlines.counts)), stringAsFactors=FALSE)
names(cbHeadlines.df) <- c("word", "frequency")
cbHeadlines.df$frequency <- as.numeric(cbHeadlines.df$frequency)

cbHeadlines.occurence <- sapply(1:nrow(cbHeadlines.matrix),
                              function(i) {length(which(cbHeadlines.matrix[i,] > 0))/ncol(cbHeadlines.matrix)})
cbHeadlines.density <- cbHeadlines.df$frequency/sum(cbHeadlines.df$frequency)

cbHeadlines.df <- transform(cbHeadlines.df, density=cbHeadlines.density, occurence=cbHeadlines.occurence)

headlines <- headlines$title

headlines.matrix <- as.matrix(get.tdm(headlines))
headlines.counts <- rowSums(headlines.matrix)
headlines.df <- data.frame(cbind(names(headlines.counts),
                                   as.numeric(headlines.counts)), stringAsFactors=FALSE)
names(headlines.df) <- c("word", "frequency")
headlines.df$frequency <- as.numeric(headlines.df$frequency)

headlines.occurence <- sapply(1:nrow(headlines.matrix),
                                function(i) {length(which(headlines.matrix[i,] > 0))/ncol(headlines.matrix)})
headlines.density <- headlines.df$frequency/sum(headlines.df$frequency)

headlines.df <- transform(headlines.df, density=headlines.density, occurence=headlines.occurence)

print("clickbait")
head(cbHeadlines.df[with(cbHeadlines.df, order(-occurence)),])
print("non-clickbait")
head(headlines.df[with(headlines.df, order(-occurence)),])





