from mrjob.job import MRJob   # MRJob version

class Nodes (MRJob):  #MRJob version
   def mapper(self, key, line):
      left, right = line.split(" ")
      if right[0] =='1':
         yield left, 1
      
   def reducer(self, key, values):
      total  = sum(values)
      if total >=10:
         yield(key, total)
 
if __name__ == '__main__':
   Nodes.run()   # MRJob version
