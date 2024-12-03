import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext

object q3{
    def omer (sc: SparkContext) = {
        val data = sc.textFile("/datasets/flight") //load data
        /*for test val data = List("2021119,2021,1,CAE,South Carolina,FLL,Florida,1.00", "2021119,2021,1,CAE,South Carolina,FLL,Florida,2.00")*/
        val fields = data.map(_.split(",")) //split by comma
        val noheader = fields.filter(x => !(x(1).contains("YEAR"))) // filtering out header
        val kvpairs = sc.parallelize(noheader.map(x => (x(0).toInt,1))) //take out header and make itin key and 1 val
        val out = kvpairs.reduceByKey((x,y) => x+y) //my output produces the right answer but they key and value are flipped
}
}
//my rdd runs in the terminal when i paste the lines but doesnt run if i use load. the correct answer is produced with the key and value flipped. 
