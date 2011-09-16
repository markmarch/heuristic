package edu.nyu.hps.mint

object ExchangeNumber {
  
  def getExchangeNumber(d: List[Int]) = {
    val (exactExchange, entries) = ExactExchangeNumber.getExactExchangeNumber(d)
    
    def getExchangeNumber(price: Int) = {
      val money = price to (99 + price)
      money.foldLeft((Int.MaxValue, 0, List(0)))((currentMin, m) => {
        val coinToSeller = if (m < 100) m else m - 100
        val coinFromSeller = m - price
        val (exchange, entry) = 
          ((exactExchange(coinToSeller) + exactExchange(coinFromSeller)),
              (entries(coinToSeller), entries(coinFromSeller)).zipped.map{_ - _})
        if ( exchange < currentMin._1) {
          (exchange, m, entry) 
        } else currentMin
      })
    }
    
    (1 to 99).map(getExchangeNumber).toList
  }
}