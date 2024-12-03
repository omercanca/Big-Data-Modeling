import csv
from mrjob.job import MRJob

class Omer(MRJob):
    # Initialize the cache as a class variable
    cache = {}

    def mapper(self, key, line):
        parts = list(csv.reader([line]))[0]
        if parts[7] != "Country":
            invoiceno = parts[0]
            sc = parts[1]
            up = float(parts[5])
            yield invoiceno, (sc, up)

    def reducer(self, key, values):
        # Check if the result is already in the cache
        if key in self.cache:
            yield key, self.cache[key]
        else:
            val = list(values)

            max_price = -1
            most_expensive_item = None
            for item in val:
                if item[1] > max_price:
                    max_price = item[1]
                    most_expensive_item = item

            # Store the result in the cache before yielding
            self.cache[key] = most_expensive_item[0]
            yield key, self.cache[key]

if __name__ == '__main__':
    Omer.run()
