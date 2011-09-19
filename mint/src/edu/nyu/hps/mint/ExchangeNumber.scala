package edu.nyu.hps.mint

object ExchangeNumber {
  // initialize an array filled with Int.MaxValue except the first one
  def initialize(denomination: List[Int]) = {
    val m = Array.fill(200)(Int.MaxValue - 1)
    m(0) = 0
    for {
      change <- 1 to 100
      coin <- denomination
    } {
      if (coin == 100 && m(change) > m(100 - change))
        m(change) = m(100 - change)
      else if (change >= coin && m(change) > m(change - coin) + 1)
        m(change) = m(change - coin) + 1
    }
    m
  }

  def getExchangeNumber(m: Array[Int], d: List[Int], price: Int) = {
    for (coin <- d) {
      if (coin == 100 && m(price) > m(price - 100)) {
        m(price) = m(price - 100)
      } else if (m(price) > m(price - coin) + 1) {
        m(price) = m(price - coin) + 1
      }
    }
    m(price)
  }

  def calculateScore(denomination: List[Int], minScore: Double, n: Double) = {
    val m = initialize(denomination)
    def continueAdd(score: Double, money: Int): Tuple2[Boolean, Double] = {
      if (money > 199)
        (true, score)
      else {
        val number = getExchangeNumber(m, denomination, money)
        val newScore = score + (if (money % 5 == 0) number * n else number)
        if (newScore < minScore)
          continueAdd(newScore, money + 1)
        else (false, score)
      }
    }
    continueAdd(0.0, 101)
  }

  def findOptimal(n: Double) = {
    var d = List(1, 5, 10, 25, 50)
    var minScore = calculateScore(d, Double.MaxValue, n)._2
    var optimal = (d, minScore)
    for {
      i <- 1 to 46
      j <- i + 1 to 47
      m <- j + 1 to 48
      k <- m + 1 to 49
      t <- k + 1 to 50
    } {
      d = List(i, j, m, k, t)
      val (better, newScore) = calculateScore(d, optimal._2, n)
      if (better) optimal = (d, newScore)
    }
    optimal
  }
}