import csv
from mrjob.job import MRJob

class Omer(MRJob):
    def mapper(self, key, line):
        parts = list(csv.reader([line]))[0]
        if parts[7] != "Country":
            invoiceno = parts[0]  # set invno to index0
            sc = parts[1]  # index for stock code
            up = float(parts[5])  # float as unit price 
            yield invoiceno, (sc, up)  # yield invno and sc

    def reducer(self, key, values):
        val = list(values)  # change values into a list of values

        # Find the stock code of the most expensive item in the order
        max_price = -1
        most_expensive_item = None
        for item in val: #for every item in our list of values
            if item[1] > max_price: # if the second element is greater than max_price
                max_price = item[1] #make max price equal to the second element
                most_expensive_item = item #make the mei the item

        yield key, most_expensive_item[0] #yield key and mei for the key

if __name__ == '__main__':
    Omer.run()
