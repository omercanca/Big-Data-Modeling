from mockmr import MockMR
import random
import hashlib

class MyMR2(MockMR):
     # pretend we have 3 reducers
    def mapper(self, key, value):
        words = value.split()
        for w in words:
            yield w,1
            # we need 3 because each reducer needs to know
            yield "total_0", 1   # hopefully this goes to reducer 0, telling it to increment
                               # total number of words by 1
            yield "total_1", 1   # goes to reducer 1
            yield "total_2", 1   # goes to reducer 2, need to change partitioner to make sure 
                               # right reducer gets the right message

    def reducer_init(self):
        self.total_number_of_words = 0

    def reducer(self, key, values_iterator):
        if key.startswith("total_"):
            self.total_number_of_words = sum(values_iterator)
        else:
            yield key, sum(values_iterator) / self.total_number_of_words

    def partition(self, key, num_reducers):
        if key.startswith("total_"):
            (junk, number) = key.split("_", 2)
            return int(number)
        return int(hashlib.sha1(bytes(str(key),'utf-8')).hexdigest(), 16) % num_reducers

    def compare(self, a, b):
        """ This is the default sorter that you get if you do not implement it.
            returns -1 if a<b, 0 if a=b, 1 if a>b """
        if a == b:
            return 0
        if a.startswith("total_") and b.startswith("total_"):
            return (a > b) - (b > a)
        if a.startswith("total_"): # b cannot be "total_"
            return -1
        if b.startswith("total_"): # a cannot be "total_"
            return 1
        return (a > b) - (b > a) # neither a nor b are "total_"



if __name__ == "__main__":
    MyMR2.run(trace=True)
