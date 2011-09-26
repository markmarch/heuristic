package edu.nyu.hps.tsp

case class City(val number: Int, x : Double, y : Double*) {
  val cordinates = x :: (y.toList)
  def distanceTo(that : City)  = City.distanceBetween(this, that)
  
  override def toString = number + ":[" + cordinates.mkString(",") + "]"
}

object City {
  def distanceBetween(cityA: City, cityB : City) = {
    Math.sqrt((cityA.cordinates, cityB.cordinates).zipped.map((a, b) => (a - b)* (a - b)).sum)
  }
}