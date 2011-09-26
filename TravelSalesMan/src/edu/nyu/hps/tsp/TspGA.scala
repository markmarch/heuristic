package edu.nyu.hps.tsp

import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec
import scala.io.Source

object TspGA {
  def main(args : Array[String]) : Unit = {
//    val map = readData(Source.fromFile("a280.tsp").getLines)
//    val tour = new Tour(shuffle((0 to 19).toList), map)
//    val mutated = tour.mutate()
//    println("Tour cost: " + tour.fitness + "\nMutated cost:" + mutated.fitness)
    time {
      findOptimal("a280.tsp")
    }
  }
  
  def findOptimal(fileName : String) {
    val map = readData(Source.fromFile(fileName).getLines())
    val populationSize = 20
    val generationSize = 3
    val mutationRate = 0.05
    val eliminationRate = 0.6
    
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
