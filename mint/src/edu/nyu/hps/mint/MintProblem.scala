package edu.nyu.hps.mint

object MintProblem {

  def main(args: Array[String]) = {
    exchangeProblem(args(0).toDouble)
  }

  def exactExchangeProblem() {
    val (denomination, score) = ExactExchangeNumber.findOptimal(4.0)
    val solution = ExactExchangeNumber.constructSolution(denomination)
    println("Score:" + score + "\nDemnomination:" + denomination)
    for (price <- 1 to 99) printExchange(price, solution(price))
  }

  def exchangeProblem(n: Double) {
    val (d, score) = ExchangeNumber.findOptimal(n)
    println("d:" + d + ", score:" + score)
    val exchange = ExchangeNumber.constructSolution(d)
    val entries = exchange.drop(101)
    val output = "{\"denominations\": [" + d.mkString(",") + "],\n" +
      "\"exchanges\":[" + entries.map((entry: List[Int]) => "[" + entry.mkString(",") + "]").mkString(",\n") + "]}"
    println(output)
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