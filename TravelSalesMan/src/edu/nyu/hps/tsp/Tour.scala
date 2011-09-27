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
      (xs, ys) match {
        case (Nil, Nil) => {
          val tempList = list ++ (shuffle(for (city <- cities if !list.contains(city)) yield city))
          new Tour(tempList.toList, map)
        }
        case (Nil, y :: rest) => {
          if (list.contains(y))
            addToTour(Nil, Nil, list)
          else
            addToTour(Nil, rest, list :+ y)
        }
        case (x :: rest, Nil) => {
          if (list.contains(x))
            addToTour(Nil, Nil, list)
          else
            addToTour(rest, Nil, x +: list)
        }
        case _ => {
          if (list.contains(xs.head))
            addToTour(Nil, ys, list)
          else if (list.contains(ys.head))
            addToTour(xs, Nil, list)
          else if (xs.head == ys.head) {
            addToTour(xs.tail, ys.tail, xs.head +: list)
          } else addToTour(xs.tail, ys.tail, xs.head +: list :+ ys.head)
        }
      }
    }

    // select a random city
    val r = cities(random.nextInt(cities.length))
    val indexA = cities.indexOf(r)
    val indexB = tour.cities.indexOf(r)
    val left = (cities.drop(indexA + 1) ::: cities.take(indexA)).reverse
    val right = tour.cities.drop(indexB + 1) ::: tour.cities.take(indexB)
    addToTour(left, right, new DList[Int](r, new DList[Int]))
  }

  def mutate() = {
    def twoOpt(tour : Tour) = {
      var (pair, reduce) = ((tour.path(0), tour.path(1)), Double.MinValue)
      for {
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
      // println(reduce)
      if (reduce > 0) {
        val (a, b, c, d) = (pair._1.from, pair._1.to, pair._2.from, pair._2.to)
        val (indexA, indexC) = (tour.cities.indexOf(a) + 1, tour.cities.indexOf(c) + 1)
        val list = tour.cities.take(indexA) ::: (tour.cities.slice(indexA, indexC).reverse) ::: tour.cities.drop(indexC)
        new Tour(list, map)
      } else {
        tour
      }
    }

    @tailrec def optimize(oldTour : Tour, newTour : Tour, count : Int) : Tour = {
      // println(oldTour.fitness - newTour.fitness)
      if (oldTour.fitness <= newTour.fitness || count == 0) {
        oldTour
      } else {
        optimize(newTour, twoOpt(newTour), count -1)
      }
    }
    optimize(this, twoOpt(this), 50)
  }
}