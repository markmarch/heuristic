package edu.nyu.hps.tsp
import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec

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
    val r = cities(random.nextInt(cities.length))
    val left = cities.take(cities.indexOf(r) - 1).reverse
    val right = tour.cities.drop(tour.cities.indexOf(r))
    addToTour(left, right, new DList[Int](r, new DList[Int]))
  }

  def mutate() = {
    def twoOpt(tour : Tour) = {
      var (pair, reduce) = ((tour.path(0), tour.path(1)), Double.MinValue)
      val opts = for {
        i <- 0 until tour.path.length; 
        j <- i + 1 until tour.path.length
        val p = (tour.path(i), tour.path(j))
        val (a, b, c, d) = (p._1.from, p._1.to, p._2.from, p._2.to)
        val delta = map(a)(b) + map(c)(d) - map(a)(c) - map(b)(d)
        if delta > reduce
      } {
        pair = p
        reduce = delta
      }
      
      if (reduce > 0) {
        val (a, b, c, d) = (pair._1.from, pair._1.to, pair._2.from, pair._2.to)
        val (indexA, indexC) = (tour.cities.indexOf(a) + 1, tour.cities.indexOf(c) + 1)
        val list = tour.cities.take(indexA) ::: (tour.cities.slice(indexA, indexC).reverse) ::: tour.cities.drop(indexC)
        new Tour(list, map)
      } else {
        tour
      }
    }

    @tailrec def optimize(oldTour : Tour, newTour : Tour) : Tour = {
      println(oldTour.fitness - newTour.fitness)
      if (oldTour.fitness <= newTour.fitness) {
        oldTour
      } else {
        optimize(newTour, twoOpt(newTour))
      }
    }
    optimize(this, twoOpt(this))
  }
}