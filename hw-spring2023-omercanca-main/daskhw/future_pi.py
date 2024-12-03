import time 
import random
from dask.distributed import Client, wait, progress, as_completed

def pi(n)
    t = 0 # first set total to 0
    for i in range(n): # for every number from 1 to n
        x = random.uniform(-1,1) # get random number from -1 to 1
        y = random.uniform(-1,1) # above line again
        if x*x + y*y <=1: # if the x^2 + y^2 is <=1
            t = t+1 # t keeps track of whether its in the circlce or not
    z = (4*t) / n # pi estimate
    return z # save z
    with client(n_workers=4) as c: # client
        y = c.map(z, n) # for i in n, the workers perform z to give us an estimate of p testi
        return sum(y/4) #return the sum of the 4 y's divided by 4, to get the average from our four workerse
