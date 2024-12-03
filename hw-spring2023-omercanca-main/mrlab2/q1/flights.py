from mrjob.job import MRJob   # MRJob version

# Change the class name!!
class Flights (MRJob):  #MRJob version
    def mapper(self, key, line): 
        words = line.split(',') #split by comma to create list
        if words[7] != "PASSENGERS": #if seventh element isn's passengers, begin this
            counter = 0 #set counter to 0
            index1 = 5 #set index1 to 5
            passengers = words[7] #let passengers equal to seventh element/column
            while counter < 2: #only want counter to go through it twice
            	yield (words[index1], float(passengers)) #store the fifth index and passengers float
            	counter += 1 #add one to counter
            	index1 = 3 #now let the index be 3 to access the third element (where they are leaving from)
            	passengers = 0 - float(passengers) #now let passengers be 0 - the number of passengers

    def reducer(self, key, values): #reducer will add it all up. first elemtn is the key and second is the sum of passengers
        yield (key, sum(values))

if __name__ == '__main__':
    Flights.run()   # MRJob version
