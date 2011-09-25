package edu.nyu.hps.tsp

import scala.util.Random
import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec

object TspGA {
  def main(args : Array[String]) : Unit = {
    val map = readData(args(0))
    val tour1 = new Tour(shuffle((0 to map.length - 1).toList), map)
    val tour2 = new Tour(shuffle((0 to map.length - 1).toList), map)
    println("Tour 1 cost :" + tour1.fitness)
    val newTour = tour1.mutate()
    println("New Tour cost:" + newTour.fitness + "\n" + newTour.cities.mkString("--"))
  }

  def readData(fileName : String) = {
    // create a City object with given data as "city_number x y z"
    def createCity(line : String) = {
      val data = line.split(" ").flatMap((token : String) => Some(token.toInt))
      new City(data(0), data(1), data(2), data(3))
    }

    import scala.io.Source
    val cities = (for (line <- Source.fromFile(fileName).getLines) yield createCity(line)).toList
    val map = new Array[Array[Double]](cities.length)
    for (i <- 0 until cities.length) {
      map(i) = new Array[Double](cities.length)
      for (j <- i + 1 until cities.length) {
        map(i)(j) = cities(i).distanceTo(cities(j))
      }
    }
    // fill in the blanks
    for (i <- 0 until cities.length; j <- i + 1 until cities.length)
      map(j)(i) = map(i)(j)
    map
  }
}
