package edu.nyu.hps.mint

import scala.collection.mutable.ListBuffer

object ExactExchangeNumber {
  // get the exact exchange number for price between 1 and 99 cents
  // for the given denomination
  def getExactExchangeNumber(d: List[Int]) = {
    // get exact exchange number for each price that uses
    // only one coin
    def makeChange(index: Int, length: Int) = {
      val change = new Array[Int](length)
      change(index) = 1
      change toList
    }
    
    val m = new Array[Int](100)
    val partition = new Array[Int](100)
    val result = new Array[List[Int]](100)
    result(0) = List(0, 0, 0, 0, 0)
    for (i <- 1 to 99) {
      if (d.contains(i)) {
        m(i) = 1
        result(i) = makeChange(d.indexOf(i), d.length)
      } else {
        m(i) = Int.MaxValue
        result(i) = result(0)
      }
    }

    for {
      i <- 1 to 99
      j <- 1 to i / 2
      if (m(i) > m(i - j) + m(j))
    } {
      m(i) = m(i - j) + m(j)
      partition(i) = j
    }

    for (i <- 1 until result.length)
      result(i) = (result(partition(i)), result(i - partition(i))).zipped.map {_ + _}
    (m toList, result toList)
  }
}

class ExactExchangeNumber {}