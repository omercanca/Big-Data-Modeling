e spaces. Do NOT use tabs.

def q1(mystring): #creating our function
  omerstuple = tuple(mystring.split('\t')) #naming the tuple. We set omerstuple equal to a tuple and split the tuple by \t using the split function
  return omerstuple[1:3] #returning the desired elements (1 and 2) of the tuple.   



def q2(mystring):#creating our function
  arr = mystring.split('\t') # first i convert the string into an array with each element being every sequence split by tab
  return arr.count('the') # i return the count of elements in the array containing the word the
   



def q3(myarray):# create our function
  omer = 0 # begin by setting omer to 0. This will be the sum of the numbers we want in the end
  for x in myarray: # for every element in my array...
   if x[0] == 'A': # if the first element in the list of lists is A...
    omer += x[1] # then take the second element in that list and add it to omer. the for loop allows us to do this for every list in the list of lists.
  return omer # return the sum of all the second elements that were chosen



def q4(myarray):# create our function
  counter = 0 # set a counter equal to 0. The counter is used to keep track of position as we move through the array
  for i in myarray: # for every element in myarray
    if i % 2==0: # we use modulus to tell if it is even. If a number is even, then i % 2 would be 0. But if it's 1, then it's odd
      return counter # if its even, return the counter
      break # end the if loop
    counter +=1 #add the position of the counter
  else: # else, the loop finishes without finding an even number after searching every index.
    return -1 # because of this, we return -1

def q5(myarray):# create our function
  x = {} # create an empty dictionary. This will be where our final data values get stored
  for a, b in myarray: # for every key:value in the inputted array...
    if a in x: # if a is in the dictionary 
      x[a] += b # add it to the total of b. This will sum up all the values with keys that are not unique.
    else: # else...
      x[a] = b # if there is a key that is unique, then its value does not change when in x
  return x #return the dictionary x with the sum of all of the values.
