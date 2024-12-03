from mrjob.job import MRJob   # MRJob version

class Nodes (MRJob):  #MRJob version
   def mapper(self, key, line):
      source, dest = line.strip().split() #strip by line where first el is source and second is dest
      yield int(source), 1 #for each source we yield 1
      yield int(dest), 1 # for each dest we yield 1
    
   def reducer(self, key, values):
      total = sum(values) #reduceer sums up each 1 that was yielded
      yield (key, total) #yield the key and the total times the key was yielded

if __name__ == '__main__':
   Nodes.run()   # MRJob version
