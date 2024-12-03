from mockmr import MockMR
import random

class MyMR2(MockMR):


    def mapper(self, key, value):
        words = value.split()
        for w in words:
            yield w,1






    def reducer(self, key, values_iterator):
        yield key, sum(values_iterator) 





if __name__ == "__main__":
    MyMR2.run(trace=True)
