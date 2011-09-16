package edu.nyu.hps.mint

case class Exchange(val price:Int, val exchange:List[Int]) {
  val numberOfCoins = exchange.foldLeft(0)((b, a) => b + a.abs)
  def cost(n: Double): Double = if (price % 5 == 0) n * numberOfCoins else numberOfCoins
}