package edu.nyu.hps

import scala.util.Random

/**
 * Package level methods.
 */
package object tsp {
  // random seed
  val random = new Random(1316995487325L)
  val minTime = 1000L
  
  // swap 2 elements in an array
  def swap[T](i : Int, j : Int, array : Array[T]) = {
    val temp = array(i)
    array(i) = array(j)
    array(j) = temp
  }

  // shuffle an list of elements in random order
  def shuffle[T](xs : List[T])(implicit m: scala.reflect.Manifest[T]) = {
    val array = (for (i <- 0 until xs.length) yield xs(i)).toArray
    for (i <- array.length - 1 to 1 by -1) {
      swap(i - 1, random.nextInt(i), array)
    }
    array.toList
  }
  
  // time how long a function call takes
  def time(f : =>Unit) = {
    val start = System.currentTimeMillis
    f
    val timeUsed = System.currentTimeMillis - start
    println("Time Used: " + timeUsed + " ms")
  }
}