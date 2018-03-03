library(tm)
library(ggplot2)

getwd()

setwd("C:/Users/floppi/Desktop/newsAPI")


# Get Clickbait train and headlines train from csv file 
cbHeadlines <- read.csv(file="cb_headlines.csv", header=TRUE, sep=";", quote="")
headlines <- read.csv(file="headlines.csv", header=TRUE, sep=";", quote="")

# Get Clickbaint and headlines from scv file
cbHeadlines2 <- read.csv(file="cb_headlines2.csv", header=TRUE, sep=";", quote="")
headlines2 <- read.csv(file="headlines2.csv", header=TRUE, sep=";", quote="")

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

#### FORMAT CLICKBAIT DATA #####

# Classify only the headnlines and not the description
cbHeadlines <- cbHeadlines$title

# get the term docuement matrix
cbHeadlines.matrix <- as.matrix(get.tdm(cbHeadlines))
# Get the frequency of words int the headlines
cbHeadlines.counts <- rowSums(cbHeadlines.matrix)
# Create a dataframe with the words in one column and counts in the other
cbHeadlines.df <- data.frame(cbind(names(cbHeadlines.counts),
                                 as.numeric(cbHeadlines.counts)), stringAsFactors=FALSE)

names(cbHeadlines.df) <- c("word", "frequency")
cbHeadlines.df$frequency <- as.numeric(cbHeadlines.df$frequency)

# Find the percentage of every word of the entire corpus
cbHeadlines.occurence <- sapply(1:nrow(cbHeadlines.matrix),
                              function(i) {length(which(cbHeadlines.matrix[i,] > 0))/ncol(cbHeadlines.matrix)})
cbHeadlines.density <- cbHeadlines.df$frequency/sum(cbHeadlines.df$frequency)

cbHeadlines.df <- transform(cbHeadlines.df, density=cbHeadlines.density, occurence=cbHeadlines.occurence)

#### FORMAT HEADLINES DATA #####

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

#Print result of formatting

print("clickbait")
head(cbHeadlines.df[with(cbHeadlines.df, order(-occurence)),])
print("non-clickbait")
head(headlines.df[with(headlines.df, order(-occurence)),])

### CLASSIFICATION ###

classify.headline <- function(path, training.df, prior = 0.5, c = 1e-6){
  msg.tdm <- as.matrix(get.tdm(path))
  
  msg.freq <- rowSums(as.matrix(msg.tdm))
  
  # Find intersections of words
  msg.match <- intersect(names(msg.freq), training.df$term)
  if(length(msg.match) < 1){
    return(prior*c^(length(msg.freq)))
  }
  else{
    match.probs <- training.df$occurence[match(msg.match, training.df$term)]
    return(prior * prod(match.probs) * c^(length(msg.freq)-length(msg.match)))
  }
}

clickbait.cbTest <- sapply(cbHeadlines2$title, function(title) classify.headline(title, training.df=cbHeadlines.df))
clickbait.headlineTest <- sapply(cbHeadlines2$title, function(title) classify.headline(title, training.df=headlines.df))

head(clickbait.cbTest)

cb.res <- ifelse(clickbait.cbTest > clickbait.headlineTest, TRUE, FALSE)

head(cb.res)

summary(cb.res)
