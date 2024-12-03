import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder().appName("Average Calculation").getOrCreate()
import spark.implicits._

val localversion = List(("2", 10000), ("2", 5000)) //list for testing
val df = localversion.toDF("key", "value")

val filtered = df.filter($"value" % 2 === 0)
val grouped = filtered.groupBy("key").agg(sum("value").alias("sum"), count("value").alias("count"))

val averageDF = grouped.withColumn("average", $"sum" / $"count").drop("sum", "count")

averageDF.show()
