from mrjob.job import MRJob   # MRJob version

CUTOFF = 300
#for every number on the left, this function will show the average of right when right is even if greater than cutoff

class LastLab(MRJob):  #MRJob version
    def mapper_init(self):
        self.cache = {}

    def mapper(self, key, line):
        parts = line.split(" ") #splits by space
        left = int(parts[0]) #left is first element
        right = int(parts[1]) #right is second
        if right not in self.cache:
            self.cache[right]=0
        if right % 2 == 0: #if right element is even
            self.cache[right] +=1
            yield left, (right, self.cache[right])
            #yield left element, right element 1

    def reducer(self, key, values):
        total = 0
        amount = 0
        for (a_number, a_count) in values:
            total = total + a_number #for every key, add the first number in the values pair
            amount = amount + a_count #for every key, add the second number in the values pair 
        average = total/amount #avg is left element/right element
        if average > CUTOFF: #if the average is greater >300
            yield key, average #yield key,avg

if __name__ == '__main__':
    LastLab.run()   # MRJob version                                         
