from mrjob.job import MRJob
class Omer(MRJob):
        def mapper_init(self): # initializing mapper
                self.cache={} # dictionary for cache
        def mapper(self, key, line):
                words = line.split() # split the input line into words
                for word in words: # for each word in the list of words
                        key = (word[0], len(word)) # key fo rtuple for the first letter nad length of the word
                        if key in self.cache: #if the key is in cache
                                self.cache[key] += 1 #add it
                        if key not in self.cache: #if its not 
                                self.cache[key] = 1 #set equal to 1
                        if (len(self.cache)>50): #if more than 50 entries
                                for key, value in self.cache.items(): # loop over it 
                                        yield(key,value) #yield the key and value
                                cache.clear() #clear the cache
        def mapper_final(self):
                if len(self.cache) > 0: # if the items in the cache still exist
                        for key, value in self.cache.items (): # sum up the items in it
                                yield(key, value) #report the key and values
        def reducer(self, key, values):
                total = sum(values) # total 
                yield(key, total)
if __name__ == '__main__':
    Omer.run()   # MRJob version
