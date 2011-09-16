package edu.nyu.hps.mint

object MintProblem {
  def main(args: Array[String]) {
    val denomination = List(1, 5, 10, 25, 50)
//    val (coinNumber, exchange) = ExactExchangeNumber.getExactExchangeNumber(denomination)
//    for(i <- 1 until exchange.length) {
//      printExchange(i, exchange(i))
//    }
//    
    time{
      print(ExactExchangeNumber.getExactExchangeNumber(denomination))
    }
    val exchangeNumber = ExchangeNumber.getExchangeNumber(denomination)
    var score = 0.0
    for(i <- 0 until 99) {
      val (numberOfCoins, moneyPaid, entry) = exchangeNumber(i)
      val exchange = Exchange(i+1, entry)
      score += exchange.cost(2)
    }
  }

  def printExchange(price: Int, exchange: List[Int]) {
    println(price + (" " * (2 - price.toString.length)) + " [" + exchange.foldLeft("")((a, b) => a + " " + b)
      + "]")
  }
  
  def time(f: => Unit){
    val startTime = System.currentTimeMillis
    f
    println("\nTime used: " + (System.currentTimeMillis - startTime) + " ms")
  }
}