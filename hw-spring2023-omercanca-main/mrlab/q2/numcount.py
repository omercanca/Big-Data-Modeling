from mrjob.job import MRJob   # MRJob version

# Change the class name!!
class Omer2(MRJob):  #MRJob version
    def mapper(self, key, line):
        words = line.split()
        for w in words:
            yield (w, 1)

    def reducer(self, key, values):
        valList = sum(values)
	if valList > 9:
            yield (key, valList)


if __name__ == '__main__':
    Omer2.run()   # MRJob version

