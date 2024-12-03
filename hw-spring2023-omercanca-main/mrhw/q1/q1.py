from mrjob.job import MRJob   # MRJob version

class Omer(MRJob):  #MRJob version
    def mapper(self, key, line):
        words = line.split()
        for w in words:
            letter = w[0] # Let letter be the first word of every word
            length = len(w) # length of word
            yield (letter, length),1 # Yield the letter and amount

    def reducer(self, key, values):
        total = sum(values)
        yield (key, total)

if __name__ == '__main__':
    Omer.run()   # MRJob version        Omer.run()   # MRJob version
