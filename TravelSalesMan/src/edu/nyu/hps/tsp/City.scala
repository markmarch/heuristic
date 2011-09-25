package edu.nyu.hps.tsp

class City(val number: Int, x : Int, y : Int, z : Int) {
  val cordinates = List(x, y, z)
  def distanceTo(that : City)  = City.distanceBetween(this, that)
  
  override def toString = number + ":[" + cordinates.mkString(",") + "]"
}

object City {
  def distanceBetween(cityA: City, cityB : City) = {
    Math.sqrt((cityA.cordinates, cityB.cordinates).zipped.map((a, b) => (a - b)* (a - b)).sum)
  }
}