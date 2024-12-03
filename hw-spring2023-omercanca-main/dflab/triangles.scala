import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
object Triangles {
   def count(spark: SparkSession): Long = {            
      val df = spark.read.format("text").load("/datasets/facebook")
      val df2 = df
         .withColumn("src", split(col("value"), " ")(0))
         .withColumn("dst", split(col("value"), " ")(1))
         .select("src","dst") //splitting by spaces to get src and sdt
      val df3 = df2.selectExpr("src as src3", "dst as dst3")
      val df4 = df2.selectExpr("src as src4", "dst as dst4") //renaming for name conflicts
      val tri = df2.join(df4, col("src") === col("src4"))
                   .join(df3, col("dst") === col("dst3")) //join df4 with df2 where src and src4  match. join df3 with df2 where dst and dst3 match
      tri.count() 
}}

