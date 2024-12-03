from mrjob.job import MRJob   # MRJob version

CUTOFF = 300

class LastLab(MRJob):  #MRJob version
    def mapper(self, key, line):
        parts = line.split(" ")
        left = int(parts[0])
        right = int(parts[1])
        if right % 2 == 0:
           yield left, (right,1)

    def reducer(self, key, values):
        total = 0
        amount = 0
        for (a_number, a_count) in values:
            total = total + a_number
            amount = amount + a_count
        average = total/amount
        if average > CUTOFF:
            yield key, average

if __name__ == '__main__':
    LastLab.run()   # MRJob version
