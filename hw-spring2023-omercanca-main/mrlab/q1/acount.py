from mrjob.job import MRJob   # MRJob version

# Change the class name!!
class Omer(MRJob):  #MRJob version
    def mapper(self, key, line):
        words = line.split()
        for w in words:
            if 'a' in w: # if the letter a, in lwoercase form, is in a word
                yield (w, 1) #display the word with the amount of a's in it

    def reducer(self, key, values):
        yield (key, sum(values))

if __name__ == '__main__':
    Omer.run()   # MRJob version
