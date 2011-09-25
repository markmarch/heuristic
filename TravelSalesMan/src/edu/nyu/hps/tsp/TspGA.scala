package edu.nyu.hps.tsp

import scala.util.Random
import scala.collection.mutable.DoubleLinkedList

object TspGA {
  def main(args : Array[String]) : Unit = {
    val map = readData(args(0))
    val ch1 = new Chromosome(shuffle((0 to map.length - 1).toList))
    val ch2 = new Chromosome(shuffle((0 to map.length - 1).toList))
    println(ch1.mate(ch2))
  }

  def readData(fileName : String) = {
    // create a City object with given data as "city_number x y z"
    def createVertex(line : String) = {
      val data = line.split(" ").flatMap((token : String) => Some(token.toInt))
      new Vertex(data(0), data(1), data(2), data(3))
    }

    import scala.io.Source
    val vertices = (for (line <- Source.fromFile(fileName).getLines) yield createVertex(line)).toList
    val map = new Array[Array[Double]](vertices.length)
    for (i <- 0 until vertices.length) {
      map(i) = new Array[Double](vertices.length)
      for (j <- i + 1 until vertices.length) {
        map(i)(j) = vertices(i).distanceTo(vertices(j))
      }
    }
    map
  }

  def swap[T](i : Int, j : Int, array : Array[T]) = {
    val temp = array(i)
    array(i) = array(j)
    array(j) = temp
  }

  def shuffle(xs : List[Int]) = {
    val array = (for (i <- 0 until xs.length) yield xs(i)).toArray
    for (i <- array.length - 1 to 2 by -1) {
      swap(i - 1, Random.nextInt(i), array)
    }
    array.toList
  }

}

class Chromosome(val tour : List[Int]) {
  // get the fitness value of this chromosome
  def fitness(map : Array[Array[Double]]) = {
    tour.zip(tour.tail ::: List(tour.head)).map { (edge) => map(edge._1)(edge._2) }.sum
  }

  // mate this chromosome with another chromosome to produce
  // a child
  def mate(chrom : Chromosome) = {
    def addToTour(xs : List[Int], ys : List[Int], list : DoubleLinkedList[Int]) : List[Int] = {
      if (xs == Nil) {
        (list ++ TspGA.shuffle(xs)).toList
      } else if (ys == Nil) {
        (list ++ TspGA.shuffle(xs)).toList
      } else {
        addToTour(xs.tail, ys.tail, xs.head +: list :+ ys.head)
      }
    }
    val r = Random.nextInt(tour.length)
    val left = tour.take(tour.indexOf(r))
    val right = chrom.tour.drop(chrom.tour.indexOf(r))
    val newTour = addToTour(left, right, new DoubleLinkedList[Int])
    newTour
  }
}