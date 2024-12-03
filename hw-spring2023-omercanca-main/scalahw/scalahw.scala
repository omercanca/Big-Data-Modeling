case class Neumaier(sum: Double, c: Double)

object HW {

   def q1_countsorted(x: Int, y: Int, z:Int): Int = {
      if (x<y && y<z && x<z) {
         3}
      else if (x<y && y<z) {
         2}
      else if (x<y && y<z) {
         2}
      else if (y<z && x<z) {
         2}
      else if (x<y || y<z || x<z) {
         1}
      else {
         0}}

   def q2_interpolation(name: String, age: Int): String= {
      s"${if (age>=21) "hello" else "howdy"}, ${name}"} //if age is >=21 than print hello else howdy and display name after it

   //def q3_polynomial(arr: Seq[Double]):  Double = {
      //val (sum, _) = input.foldLeft((0.0, 0)) { case ((acc, i), value) =>
      //val newAcc = acc + value * i
      //val newIndex = i + 1
      //(newAcc, newIndex)
     // }
       //  sum}
   
   def q4_application(x: Int, y: Int, z: Int)(f: (Int, Int) => Int) = {
      f(f(x, y), z)} //kinda confused for this one and what the q is asking
   
   def q5_stringy(start:Int, n:Int): Vector[String]= {
      Vector.tabulate(n)(i => (start + i).toString) } //tabulate n, i is the number we start from + i. tabulate allows us to get all the numbers instead of just start +i

   def q6_modab(a: Int,b: Int,c: Vector[Int]): Vector[Int] = {
      c.filter(x => x >= a && x % b != 0).map(identity) } //filter c to be > a then not be a multiple of b. identity function makes it each element isnt changed
  
   
  def q7_count(arr: Vector[Int])(f: Int => Boolean): Int = {
     if (arr.isEmpty) {
        0} //if the array is 0, output should be 0
     else {
        val total = if (f(arr.head)) 1 else 0 //let total equal 1 if the head of array satisfies fl 
        total + q7_count(arr.tail)(f) //adds total to the tail
    }
}
 

  def q8_count_tail(arr: Vector[Int])(f: Int => Boolean): Int =  //didnt have time to add comments before submission{
     @annotation.tailrec
     def countTailRec(remaining: Vector[Int], acc: Int): Int = {
        if (remaining.isEmpty) {
           acc } 
        else {
           val newAcc = if (f(remaining.head)) acc + 1 else acc
           countTailRec(remaining.tail, newAcc) } }
     countTailRec(arr, 0) }

}    
