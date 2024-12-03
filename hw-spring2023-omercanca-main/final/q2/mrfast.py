from mrjob.job import MRJob   # MRJob version

class Nodes (MRJob):  #MRJob version
   def mapper_init(self):
      self.cache = {}

   def mapper(self, key, line):
      left, right = line.split(" ")
      if right not in self.cache:
         self.cache[right] = 0
      if right[0] =='1':
         self.cache[right] +=1
         yield left, self.cache[right]
      
   def reducer(self, key, values):
      total  = sum(values)
      if total >=10:
         yield(key, total)
 
if __name__ == '__main__':
   Nodes.run()   # MRJob version
