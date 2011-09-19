package edu.nyu.hps.mint

object MintProblem {
  def main(args: Array[String]) {
//    time {
//      exactExchangeProblem
//    }
    val d = List(1, 5, 10 , 25, 50, 100)
    val m = ExchangeNumber.initialize(d)
    println(m.toList.mkString(",")) 
    for(price <- 101 to 199) {
      println (ExchangeNumber.getExchangeNumber(m, d, price))
    }
  }

  def exactExchangeProblem() {
    val (denomination, score) = ExactExchangeNumber.findOptimal(4.0)
    val solution = ExactExchangeNumber.constructSolution(denomination)
    println("Score:" + score + "\nDemnomination:" + denomination)
    for (price <- 1 to 99) printExchange(price, solution(price))
  }
  
  def exchangeProblem() {
    val (denomination, score) = ExchangeNumber.findOptimal(4.0)
    println("Score:" + score + "\nDemnomination:" + denomination)
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