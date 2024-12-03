from mrjob.job import MRJob
class Nodes(MRJob):
   def mapper_init(self):
      self.cache = {}

   def mapper(self, key, line):
      source, dest = line.strip().split()
      source, dest = int(source), int(dest)
      if source not in self.cache:
         self.cache[source] = 0
      if dest not in self.cache:
         self.cache[dest] = 0
      self.cache[source] += 1
      self.cache[dest] += 1
      yield source, self.cache[source]
      yield dest, self.cache[dest]

   def reducer(self, key, values):
      total = sum(values)
      yield key, total

if __name__ == '__main__':
   Nodes.run()
