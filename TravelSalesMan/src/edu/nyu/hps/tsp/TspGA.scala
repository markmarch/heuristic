package edu.nyu.hps.tsp

import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec
import scala.io.Source

object TspGA {
  def main(args : Array[String]) : Unit = {
      val l = new Run().run(args)
      val list = l.toString().split(",").toList.map((a) => a.toInt - 1).dropRight(1)
      val map = readData(Source.fromFile(args(0)).getLines)
      val tour = new Tour(list, map)
      val mutated = tour.mutate()
      println(constructTour(mutated.cities))
  }

  def constructTour(list : List[Int]) = {
    val start = list.indexOf(0)
    (list.drop(start) ::: list.take(start)).map(_ + 1).mkString("[", ",", ", 1]")
  }
  def findOptimal(fileName : String) {
    val map = readData(Source.fromFile(fileName).getLines())
    val populationSize = 100
    val generationSize = 4
    val mutationRate = 0.2
    val eliminationRate = 0.4

    val initialPopulation = Population.generate(populationSize, map)
    val population = new Population(initialPopulation, generationSize, eliminationRate, mutationRate)
    val tour = population.findOptimal()
    println("Cost: " + tour.fitness + "\n" + tour.cities)
  }

  def readData(lines : Iterator[String]) = {
    // create a City object with given data as "city_number x y z"
    def createCity(line : String) = {
      val data = line.trim.split(""" +""").flatMap((token : String) => Some(token.toDouble))
      new City(data(0).toInt, data(1), data.drop(2) : _*)
    }

    val cities = (for (line <- lines) yield createCity(line)).toList
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
