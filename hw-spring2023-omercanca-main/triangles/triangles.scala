import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext

object Triangles {

    // A ---- B
    //  \
    //   \
    //    C
   
    // adj list format:
    // A  B  C
    // B  A
    // C  A

    // Edge list format (redundant)
    // A  B
    // B  A
    // A  C
    // C  A

    // Edge list (non-redundant)// Edge list format (redundant)
    // A  B
    // C  A



    def getFBasString(sc: SparkContext): RDD[Array[String]] = {
        val fb = sc.textFile("/datasets/facebook2").map(x => x.trim()).map(x => x.split(" "))
        fb
    }

   
    def getFB(sc: SparkContext): RDD[(Long, Long)] = {
        val fbsplit = sc.textFile("/datasets/facebook2").map(x => x.trim()).map(x => x.split(" "))
        val fb = fbsplit.map(x => (x(0).toLong, x(1).toLong))
        fb
    }

    def getToyRedundant(sc: SparkContext): RDD[(Long, Long)] = {
        sc.parallelize(List((1L,2L),(2L,1L)))
    }
    def getTrickyRedundant(sc: SparkContext): RDD[(Long, Long)] = {
        sc.parallelize(List((1L,2L),(2L,1L),(2L, 1L)))
    }
    def getToyTricky(sc: SparkContext): RDD[(Long, Long)] = {
        sc.parallelize(List((1L,2L),(2L,1L),(3L,4L)))
    }
  
    def getToyNonRedundant(sc: SparkContext): RDD[(Long, Long)] = {
        sc.parallelize(List((2L,1L)))
    }

    def toyGraph(sc: SparkContext) = {
       // create a toy graph for triangle counting
       //
       // 1 ----- 2
       // | \     |
       // |   \   |
       // |     \ |
       // 4-------3 ------ 5
       val mylist = List[(Long, Long)](
                         (1, 1),
                         (1, 2),
                         (2, 1),
                         (2, 3),
                         (3, 2),
                         (1, 3),
                         (3, 1),
                         (1, 4),
                         (4, 1),
                         (1, 3),
                         (3, 1),
                         (4, 3),
                         (3, 4),
                         (3, 5),
                         (5, 3),
                         (3, 5),
                         (5, 3),
                         (1, 3),
                         (3, 1),
                         (1, 4),
                         (4, 1),
                         (4, 3),
                         (3, 4),
                        )

       sc.parallelize(mylist)
    }

    def isAdjList(graph: RDD[Array[String]]) = {
       // returns true if graph rdd is adjacency list otherwise false
       // e.g., feed in the results of getFBasString
       // check how many nodes are in the lines. An adjacenly list will have
       // some line that has != 2 nodes, edge list always has 2 nodes in each line
       val myrdd = graph.filter{x => x.size != 2}  //keeping lines that provide evidence of adj list
       val amount = myrdd.count() // number of lines giving evidence of adj list
       amount > 0
    }
    // we ran this on FB and decided it is an edge list, so next q: redundant or not
   
    def isRedundant(graph: RDD[(Long, Long)]) = {
       // returns true if the graph is a redundant edge list
       // e.g., eed in the results of getFB
       // for each line (A, B), we want to check if (B, A) is in the rdd, but in parallel 
       val flipped = graph.map{case (a, b) => (b, a)}.distinct().persist() // the list of things we want to check
       val common = flipped.intersection(graph)
       val common_count = common.count()
       val original_count = flipped.count()
       common_count - original_count == 0 
    }

    def triangleCount(graph: RDD[(Long, Long)]) = {
        // we get rid of duplicates because they are tricky
        // Triangle: if a node has 2 friends, we look for an edge between the friends
        val distinct_edges = graph.distinct().filter{case (x,y) => x != y}
        // filter at the end gets rid of things like (1,1)
        //step 1: get an rdd which lists a node and its 2 friends
        val node_and_two_friends = distinct_edges.join(distinct_edges)
        // ("A", "B")        ("A", "D")
        // ("A", "D")        ("A", "B")
        // ("A", "C")        ("A", "C")
        // join will look like:
        // ("A", ("B", "D"))  --- this line means A is friends with B and D (also there is path from B->A->D
        // ("A", ("B", "B"))  --- this means A is friends with B and B
        // ("A", ("B", "C"))  --- A is friends with B and C
        // ("A", ("D", "D"))
        // ("A", ("D", "B"))
        // ("D", ("A", "B"))
        // ("D", ("B", "A"))
        // ("B", ("D", "A"))
        // ("B", ("A", "D"))
        //  ....
        // + more stuff
        // get rid of weird things like (A, (B, B))
        // get rid of redunancies: we don't need both ("A", ("B", "D")) and ("A", ("D", "B")) in there, only 
        // one of these is necessary
        val cleaned_node_and_two_friends = node_and_two_friends.filter{case (first, (second, third)) => second < third}
         // rdd looks something like:
        // ("A", ("B", "D"))  --- means A is friends with B and D (need to check if ("B", "D") is an edge)
        // ("A", ("B", "C"))  --- A is friends with B and C (need to check if ("B", "C") is an edge)

        // now we need to check if the value in cleaned_node_and_two_friends is an edge in distinct_edges rdd
        // we want to do a join between cleaned_node_and_two_friends and distinct_edges to match the value
        // in the left rdd to the entire entry of the right rdd. But the problem is a join only matches on keys
        //
        // ("A", ("B", "D"))             ("A", "B")    
        // ("A", ("B", "C"))             ("B", "D")
        //  ....                         ("D", "E")
        // ...                             ....
        // so we change what the keys are
        val rekey_cnatf = cleaned_node_and_two_friends.map{case (first, (second, third)) => ((second, third), first)}
        val rekey_de = distinct_edges.map{case (x,y) => ((x,y), 1)}
        // (("B", "D"), "A")             (("A", "B"), 1)    
        // (("B", "C"), "A")             (("B", "D"), 1)
        //  ....                         (("D", "E"), 1)
        // ...                             ....
        // mean: "A" friends with B,D    mean: edge "B" and "D"
        val joined = rekey_cnatf.join(rekey_de)
        // (("B", "D"), ("A", 1)) -- means: "A" has friends B and D, and there is an edge between B and D
        //                            so this is triangle. 
        joined.count()/3  // when we look at rdd, every triangle is duplicated 3 times
    }
    def triangleCount2(graph: RDD[(Long, Long)]) = {
        // we get rid of duplicates because they are tricky
        // Triangle: if a node has 2 friends, we look for an edge between the friends
        val distinct_edges = graph.distinct().filter{case (x,y) => x != y}
        // filter at the end gets rid of things like (1,1)
        //step 1: get an rdd which lists a node and its 2 friends
        val node_and_two_friends = distinct_edges.join(distinct_edges)
        // ("A", "B")        ("A", "D")
        // ("A", "D")        ("A", "B")
        // ("A", "C")        ("A", "C")
        // join will look like:
        // ("A", ("B", "D"))  --- this line means A is friends with B and D (also there is path from B->A->D
        // ("A", ("B", "B"))  --- this means A is friends with B and B
        // ("A", ("B", "C"))  --- A is friends with B and C
        // ("A", ("D", "D"))
        // ("A", ("D", "B"))
        // ("D", ("A", "B"))
        // ("D", ("B", "A"))
        // ("B", ("D", "A"))
        // ("B", ("A", "D"))
        //  ....
        // + more stuff
        // get rid of weird things like (A, (B, B))
        // get rid of redunancies: we don't need both ("A", ("B", "D")) and ("A", ("D", "B")) in there, only 
        // one of these is necessary
        // we also get rid of (B, (A, D)) and (D, (A, B)) and so on because they mean the same thing as
        // (A, (B, D))
        val cleaned1 = node_and_two_friends.filter{case (first, (second, third)) => second < third}
        val cleaned2 = cleaned1.filter{case (first, (second, third)) => first < second}
         // rdd looks something like:
        // ("A", ("B", "D"))  --- means A is friends with B and D (need to check if ("B", "D") is an edge)
        // ("A", ("B", "C"))  --- A is friends with B and C (need to check if ("B", "C") is an edge)

        // now we need to check if the value in cleaned_node_and_two_friends is an edge in distinct_edges rdd
        // we want to do a join between cleaned_node_and_two_friends and distinct_edges to match the value
        // in the left rdd to the entire entry of the right rdd. But the problem is a join only matches on keys
        //
        // ("A", ("B", "D"))             ("A", "B")    
        // ("A", ("B", "C"))             ("B", "D")
        //  ....                         ("D", "E")
        // ...                             ....
        // so we change what the keys are
        val rekey_cnatf = cleaned2.map{case (first, (second, third)) => ((second, third), first)}
        val rekey_de = distinct_edges.map{case (x,y) => ((x,y), 1)}
        // (("B", "D"), "A")             (("A", "B"), 1)    
        // (("B", "C"), "A")             (("B", "D"), 1)
        //  ....                         (("D", "E"), 1)
        // ...                             ....
        // mean: "A" friends with B,D    mean: edge "B" and "D"
        val joined = rekey_cnatf.join(rekey_de)
        // (("B", "D"), ("A", 1)) -- means: "A" has friends B and D, and there is an edge between B and D
        //                            so this is triangle. 
        joined.count()  // when we look at rdd, every triangle is duplicated 3 times
    }
}
