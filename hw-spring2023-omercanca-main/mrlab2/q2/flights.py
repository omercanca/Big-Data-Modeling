from mrjob.job import MRJob   # MRJob version

class Flights (MRJob):  #MRJob version
	def mapper(self, key, line):
        	words = line.split(',') #split into words
        	if words[5] == "FLL": #if arriving airport is fl
            		yield(words[3], (float(words[7]),0)) #yield the airport and number of passengers
        	if words[3] == "ORD": #if departing airport is chicago
            		yield(words[5],(0,float(words[7]))) #yield airport and number of passengers
	def reducer(self, key, values):
		val = list(values) #let val be a list of the values
		leave = 0 #set leaving =0
		arrive = 0 #set arrive =0
		for i in range(len(val)): #for in range of the length of values
			leave += val[i][0] #add 1 to leave
			arrive += val[i][1] # add 1 to arrive
		yield (key, (leave, arrive)) #yield the key and leave and arrive

if __name__ == '__main__':
	 Flights.run()   # MRJob version
