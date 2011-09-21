package edu.nyu.hps.mint

object ExchangeNumber {
  def initialize(denomination: List[Int]) = {
    val m = Array.fill(200)(Int.MaxValue - 1)
    val record = Array.fill(200)(0)
    m(0) = 0
    m(100) = 0
    for {
      change <- 1 to 100
      coin <- denomination
    } {
      if (coin == 100 && m(change) > m(100 - change)) {
        m(change) = m(100 - change)
        record(change) = coin
      } else if (change >= coin && m(change) >= m(change - coin) + 1) {
        m(change) = m(change - coin) + 1
        record(change) = coin
      }
    }
    (m, record)
  }

  def getExchangeNumber(m: Array[Int], record: Array[Int], d: List[Int], price: Int) = {
    for (coin <- d) {
      if (coin == 100 && m(price) > m(price - 100)) {
        m(price) = m(price - 100)
        record(price) = 100
      } else if (m(price) > m(price - coin) + 1) {
        m(price) = m(price - coin) + 1
        record(price) = coin
      }
    }
    m(price)
  }

  def calculateScore(denomination: List[Int], minScore: Double, n: Double) = {
    val (m, record) = initialize(denomination)
    def continueAdd(score: Double, money: Int): Tuple2[Boolean, Double] = {
      if (money > 199)
        (true, score)
      else {
        val number = getExchangeNumber(m, record, denomination, money)
        val newScore = score + (if (money % 5 == 0) number * n else number)
        if (newScore < minScore)
          continueAdd(newScore, money + 1)
        else (false, score)
      }
    }
    continueAdd(0.0, 101)
  }

  def findOptimal(n: Double) = {
    var d = List(1, 5, 10, 25, 50, 100)
    var minScore = calculateScore(d, Double.MaxValue, n)._2
    var optimal = (d, minScore)
    for {
      j <- 2 to 47
      m <- j + 1 to 48
      k <- m + 1 to 49
      t <- k + 1 to 50
    } {
      val d = List(1, j, m, k, t, 100)
      val (better, newScore) = calculateScore(d, optimal._2, n)
      if (better) optimal = (d.take(5), newScore)
    }
    optimal
  }
  
  def constructSolution(denomination: List[Int]): List[List[Int]] = {
    def makeChange(index: Int, length: Int) = {
      (Array.fill(index)(0) ++ (1 +: Array.fill(length - index - 1)(0))).toList
    }
    val solution = new Array[List[Int]](200)
    solution(0) = List(0, 0, 0, 0, 0)
    solution(100) = List(0, 0, 0, 0, 0)
    for (index <- 0 until denomination.length) {
      solution(denomination(index)) = makeChange(index, denomination.length)
      solution(100 + denomination(index)) = makeChange(index, denomination.length)
    }

    def addList(xs: List[Int], ys: List[Int]) = {
      (xs, ys).zipped.map { _ + _ }
    }

    def subList(xs: List[Int], ys: List[Int]) = {
      (xs, ys).zipped.map { _ - _ }
    }
    
    val d = denomination ::: List(100)
    val (m, record) = initialize(d)
    for {
      price <- 101 to 199
      coin <- d
    } {
      if (coin == 100 && m(price) > m(200 - price)) {
        m(price) = m(200 - price)
        record(price) = coin
      } else if (m(price) > m(price - coin) + 1) {
        m(price) = m(price - coin) + 1
        record(price) = coin
      }
    }

    for (price <- 1 to 100) {
      if (record(price) == 100) {
        solution(price) = solution(100 - price)
      } else {
        solution(price) = addList(solution(price - record(price)), solution(record(price)))
      }
    }
    for(price <- 1 to 100) {
      solution(price) = solution(price).map {_ * -1}
    }
    
    for (price <- 101 to 199) {
      if (record(price) == 100) {
        solution(price) = solution(price - 100)
      } else {
        solution(price) = addList(solution(price - record(price)), solution(100 + record(price)))
      }
    }
    solution.toList
  }
}