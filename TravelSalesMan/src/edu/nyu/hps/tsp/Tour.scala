package edu.nyu.hps.tsp
import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec
import scala.util.Random

class Tour(val cities : List[Int], val map : Array[Array[Double]]) {
  // type alias
  private type DList[T] = DoubleLinkedList[T]

  val path = cities.zip(cities.tail ::: List(cities.head)).map((link) => new Link(link._1, link._2))
  val fitness = path.foldLeft(0.0)((sum, link) => sum + map(link.from)(link.to))

  def crossover(tour : Tour) = {
    @tailrec def addToTour(xs : List[Int], ys : List[Int], list : DList[Int]) : Tour = {
      if (xs == Nil || ys == Nil || list.contains(xs.head) || list.contains(ys.head)) {
        val tempList = list ++ (shuffle(for (city <- cities if !list.contains(city)) yield city))
        new Tour(tempList.toList, map)
      } else {
        addToTour(xs.tail, ys.tail, xs.head +: list :+ ys.head)
      }
    }

    // select a random city
    val r = cities(Random.nextInt(cities.length))
    val left = cities.take(cities.indexOf(r) - 1).reverse
    val right = tour.cities.drop(tour.cities.indexOf(r))
    addToTour(left, right, new DList[Int](r, new DList[Int]))
  }

  def mutate() = {
    def twoOpt(tour : Tour) = {
      val array = tour.cities.toArray
      val pairs = for (linkA <- tour.path; linkB <- tour.path if linkA != linkB) yield (linkA, linkB)
      for (pair <- pairs) {
        val (a, b, c, d) = (pair._1.from, pair._1.to, pair._2.from, pair._2.to)
        if (map(a)(c) + map(b)(d) < map(a)(b) + map(c)(d))
          swap(array.indexOf(b), array.indexOf(c), array)
      }
      new Tour(array.toList, map)
    }
    
    @tailrec def optimize(oldTour : Tour, newTour : Tour) : Tour = {
      println(newTour.fitness)
      if (oldTour.fitness <= newTour.fitness) {
        oldTour
      } else {
        optimize(newTour, twoOpt(newTour))
      }
    }
    
    optimize(this, twoOpt(this))
  }
}