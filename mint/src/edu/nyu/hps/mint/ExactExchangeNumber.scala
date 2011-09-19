package edu.nyu.hps.mint

import scala.collection.mutable.ListBuffer

object ExactExchangeNumber {
  // initialize an array filled with Int.MaxValue except the first one
  def initialize() = {
    val m = Array.fill(100)(Int.MaxValue)
    m(0) = 0
    m
  }

  // get the exact exchange number for a given price with previous calculation 
  // and the denomination
  def getExactExchangeNumber(m: Array[Int], price: Int, denomination: List[Int]) = {
    for (coin <- denomination if price >= coin && m(price) > m(price - coin) + 1) {
      m(price) = m(price - coin) + 1
    }
    m(price)
  }

  // get the exact exchange solution for a given denomination
  def constructSolution(denomination: List[Int]): List[List[Int]] = {
    def makeChange(index: Int, length: Int) = {
      (Array.fill(index)(0) ++ (1 +: Array.fill(length - index -1)(0))).toList
    }
    val solution = new Array[List[Int]](100)
    solution(0) = List(0, 0, 0, 0, 0)
    for(index <- 0 until denomination.length)
      solution(denomination(index)) = makeChange(index, denomination.length) 
    
    val record = new Array[Int](100)
    val m = initialize()
    for {
      price <- 1 to 99
      coin <- denomination if price >= coin && m(price) > m(price - coin) + 1
    } {
      m(price) = m(price - coin) + 1
      record(price) = coin
    }
    for (price <- 1 to 99) {
      solution(price) = (solution(price - record(price)), solution(record(price))).zipped.map{_ + _}
    }
    solution.toList
  }

  // calculate score for a given denomination with specific N
  // returns (true, new lowest score) if this denomination results a lower score
  // than previous lowest score else returns (false, lowest score)
  def calculateScore(denomination: List[Int], minScore: Double, n: Double) = {
    val m = initialize();
    def continueAdd(score: Double, price: Int): Tuple2[Boolean, Double] = {
      if (price > 99)
        (true, score)
      else {
        val number = getExactExchangeNumber(m, price, denomination)
        val newScore = score + (if (price % 5 == 0) number * n else number)
        if (newScore < minScore)
          continueAdd(newScore, price + 1)
        else (false, score)
      }
    }
    continueAdd(0.0, 1);
  }

  // find the optimal denomination for a given N
  def findOptimal(n: Double) = {
    var d = List(1, 5, 10, 25, 50)
    var minScore = calculateScore(d, Double.MaxValue, n)._2
    var optimal = (d, minScore)
    for {
      i <- 2 to 96
      j <- i + 1 to 97
      m <- j + 1 to 98
      k <- m + 1 to 99
    } {
      d = List(1, i, j, m, k)
      val (better, newScore) = calculateScore(d, optimal._2, n)
      if (better) optimal = (d, newScore)
    }
    optimal
  }
}