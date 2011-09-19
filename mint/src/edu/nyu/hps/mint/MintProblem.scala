package edu.nyu.hps.mint

object MintProblem {
  def main(args: Array[String]) {
    time {
      exactExchangeProblem
    }
  }

  def exactExchangeProblem() {
    val (denomination, score) = ExactExchangeNumber.findOptimal(2.0)
    val solution = ExactExchangeNumber.constructSolution(denomination)
    println("Score:" + score + "\nDemnomination:" + denomination)
    for (price <- 1 to 99) printExchange(price, solution(price))
  }
  
  def printExchange(price: Int, exchange: List[Int]) {
    println(price + (" " * (2 - price.toString.length)) +" ["+ exchange.mkString(",")
      + "]")
  }

  def time(f: => Unit) {
    val startTime = System.currentTimeMillis
    f
    println("\nTime used: " + (System.currentTimeMillis - startTime) + " ms")
  }
}