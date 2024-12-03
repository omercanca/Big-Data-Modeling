import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._

object Squares {

    def main(args: Array[String]) = {
       // main is the function that starts running when you spark-submit
       val sc = getSC()
       //val dataset = getFB(...) or getToy(...)
       //val squares = countSquares(dataset)
       //now print the number of squares
    }

    def getSC() = {
       // this function is supposed to return a spark context
    }

    def getFB(sc: SparkContext): RDD[(Int, Int)] = {
       // read in the facebook dataset and return an rdd of type  RDD[(Int, Int)]
    }

    def getToy(sc: SparkContext): RDD[(Int, Int)] = {
       // return a toy dataset that will be used for testing
       //IMPORTANT: comment this function to explain the dataset you are creating
    }

    def countSquares(dataset: RDD[(Int, Int)]) = {
       // comment this function well, including an good description of the strategy
       // and also how you implement the strategy in spark

    }

}
