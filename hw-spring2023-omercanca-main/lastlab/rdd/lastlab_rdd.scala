import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object lastlab {

    def p2(sc: SparkContext) = {
        val localversion = List("2 10000", "2 5000") //list for testing
        val fields = localversion.map(_.split(" "))
        val filtered = fields.filter(x => x(1).toInt % 2 == 0)
        val kvpairs = sc.parallelize(filtered.map(x => (x(0).toInt, (x(1).toInt, 1))))
        val reduced = kvpairs.reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
        val average = reduced.mapValues(x => x._1.toDouble / x._2.toDouble)
}}
