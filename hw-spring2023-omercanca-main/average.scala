import org.apache.spark.SparkContext

object Avg {

    def average(sc:SparkContext) = {
       // For each letter, we want to know what is the average word length in war and peace
       // that starts with the letter.
       // ex:   'a': sum of the letters of all words starting with 'a'/number of words starting
       //      with 'a'
       val wap = sc.textFile("/datasets/wap")
       //plan: get an RDD where each entry is a letter and a word. ('a', "apple")
       // step 2: use aggregateByKey to group all words with the same starting letter together
       //     and then compute, for each letter, the total lenght of its words, total count of
       //     its words
       // step 3: create the avergage from step 2
       // starting from:  wap: RDD[Strings], the string is a line of the input file

       // create an RDD with multiple entries from each input line
       val words = wap.flatMap{line => "\\W+".r.split(line)}  // regular expression for splitting
                                                          // on whitespaces 
       // words is an rdd where every entry is a word (almost), but some entries are the
       // empty string, which we get when an input line is all spaces, and also we have "words"
       // that start with things that are not letters. So we want to get rid of this junk.
       // use filter
       val noblanks = words.filter{word => word.size > 0}
       val startsWithLetter = noblanks.filter{word => word(0).isLetter}
       val kv = startsWithLetter.map{word => (word.toLowerCase()(0), word)}
       //now we do aggregate by key to convert an rdd where every entry looks like ('a', 'apple')
       // into an rdd where an entry might look like ('a', (72362737,45)), 
       //  so basically (letter, (sum of word lengths, count of words))
       //aggregateByKey(initialValue)(within_node_reduce, between_node_reduce)
       val aggregated = kv.aggregateByKey((0,0))( //(0,0) means running sum of word lengths is 0
                                                  // running count of words is 0
           {case ((runninglen,runningcount), word) => (runninglen + word.size, runningcount+1)},
           {case ((mylen, mycount), (l, c)) => (mylen+l, mycount + c)}
           )
       //aggregated is an RDD where each entry looks something like ('a', (5, 2)) and we convert
       // it to ('a', 2.5)
       // warning: scala does integer division, we don't want that, so we convert something
        // to a double
      
       //val finalResult = aggregated.map{case (letter, (num, denom)) => (letter, num.toDouble/denom)}
       //The above map is correct, but all it did is change the value and not the key. In
       // this case, a better function is mapValues  (sometimes scala can optimize better)
       val finalResult = aggregated.mapValues{case (num, denom) =>  num.toDouble/denom}
       finalResult
    }


}
