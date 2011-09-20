package edu.nyu.hps.mint

object MintProblem {
  def main(args: Array[String]) {
    val (d, _) = ExchangeNumber.findOptimal(args(0).toDouble)
    val exchange = ExchangeNumber.constructSolution(d)
    val entries = exchange.drop(101).take(99)
    val output = "{\"denominations\": [" + d.mkString(",") + "],\n" +
    		"\"exchanges\":[" + entries.map((entry: List[Int]) => "[" + entry.mkString(",")+"]").mkString(",\n") + "]}"
    println(output)
//    time{
//      exchangeProblem
//    }
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
    println(price + (" " * (2 - price.toString.length)) + " [" + exchange.mkString(",")
      + "]")
  }

  def time(f: => Unit) {
    val startTime = System.currentTimeMillis
    f
    println("\nTime used: " + (System.currentTimeMillis - startTime) + " ms")
  }
}