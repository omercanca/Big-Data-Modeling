from mockmr import MockMR
import random

class LetterCount(MockMR):

    def mapper_init(self):
        self.letter_cache = {}

    def mapper(self, key, line):
        for character in line:
            if character.isalpha():
                self.letter_cache[character] = 1+self.letter_cache.get(character, 0)
                # of possible keys is so small (i.e. 52) that we don't have to worry
                # about the cache size.
                
    def mapper_final(self):
       for character in self.letter_cache:
           yield character, self.letter_cache[character]


    def reducer(self, key, values_iterator):
        yield key, sum(values_iterator) 


if __name__ == "__main__":
    LetterCount.run(trace=True)
