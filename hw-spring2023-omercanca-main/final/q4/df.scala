import org.apache.spark.sql. {Dataset, DataFrame, SparkSession, Row} import org.apache.spark.sql.catalyst.expressions.aggregate.
import org.apache.spark.sql.expressions._ import org.apache.spark.sql. functions._


object q4 {
    def df(spark: SparkSession) = {
        val data = spark.read.format("text").load("/datasets/flight")
        val new_data = data //ran out of time
}}

