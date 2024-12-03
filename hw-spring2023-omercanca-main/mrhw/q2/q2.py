from mrjob.job import MRJob
import csv
class Omer(MRJob):  #MRJob version
    def mapper(self, key, line):
        parts = list(csv.reader ([line]))[0]
        if parts[7] != "Country":
            sc = parts[1]
            quantity = int(parts[3])
            unitprice = float(parts[5])
            country = parts[7]
            total = quantity * unitprice
            yield (country, sc), (quantity, total)

    def reducer(self, key, values):
        items = 0
        price = 0
        for quantity, total in values:
            items += quantity
            price += total
        yield key, (items, price)

if __name__ == '__main__':
    Omer.run()   # MRJob version        Omer.run()   # MRJob version
