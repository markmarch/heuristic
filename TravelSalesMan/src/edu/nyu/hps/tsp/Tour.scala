package edu.nyu.hps.tsp
import scala.collection.mutable.DoubleLinkedList
import scala.annotation.tailrec

class Tour(val cities : List[Int], val map : Array[Array[Double]]) {
  // type alias
  private type DList[T] = DoubleLinkedList[T]

  lazy val path = cities.zip(cities.tail ::: List(cities.head)).map((link) => new Link(link._1, link._2))
  lazy val fitness = path.foldLeft(0.0)((sum, link) => sum + map(link.from)(link.to))

  override def toString = cities.mkString("[", ",", "]") + " " + fitness

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
    @tailrec def swapEdge(list : List[Int], length : Int, count : Int) : List[Int] = {
      if (count == 0)
        list
      else {
        // randomly select 2 cities
        val c1 = random.nextInt(length - 3)
        val c2 = c1 + 3 + random.nextInt(length - c1 - 3)
        val (a, b, c, d) = (list(c1), list((c1 + 1) % length),
          list(c2), list((c2 + 1) % length))
        if (map(a)(b) + map(c)(d) > map(a)(c) + map(b)(d))
          swapEdge(list.take(c1 + 1) ::: (list.slice(c1 + 1, c2 + 1).reverse) ::: list.drop(c2 + 1),
            length, count - 1)
        else swapEdge(list, length, count - 1)
      }
    }

    @tailrec def swapCity(array : Array[Int], pairs : List[Tuple2[Int, Int]]) : List[Int] = {
      pairs match {
        case (b, e) :: rest => {
          val length = array.length
          val (a, c, d, f) = ((b - 1 + length) % length, b + 1, e - 1, (e + 1) % length)
          val reduce = map(a)(b) + map(b)(c) + map(d)(e) + map(e)(f) -
            (map(a)(e) + map(e)(b) + map(d)(b) + map(b)(f))
          if (reduce > 0) {
            swap(b, e, array)
          }
          swapCity(array, rest)
        }
        case Nil => array.toList
      }
    }

    @tailrec def swapLink(list : List[Int], pairs : List[Tuple2[Link, Link]]) : List[Int] = {
      pairs match {
        case Nil => list
        case pair :: tail if canOptimize(pair._1, pair._2)=> {
          val length = list.length
          val List(a, b, c, d) = List(pair._1.from, pair._1.to, pair._2.from, pair._2.to).map(list.indexOf(_))
          if ((a + 1) % length == b && (c + 1) % length == d) {
            swapLink(list.take(a + 1) ::: (list.slice(a + 1, c + 1).reverse) ::: list.drop(c + 1), tail)
          } else {
            println("link broke")
            swapLink(list, tail)
          }
        }
        case _ => swapLink(list, pairs.tail)
      }
    }
    
    def canOptimize(e1 : Link, e2 : Link) = {
      val (a, b, c, d) = (e1.from, e1.to, e2.from, e2.to)
      val reduce = map(a)(b) + map(c)(d) - (map(a)(c) + map(b)(d))
      // println("reduce:" + reduce)
      reduce > 0
    }

    def twoOpt(tour : Tour) = {
      val pairs = (for {
        e1 <- tour.path;
        e2 <- tour.path; if e1 != e2 && !tour.path.contains(new Link(e1.to, e2.from))
      } yield (e1, e2)).toList
      new Tour(swapLink(tour.cities, pairs), map)
    }

    @tailrec def optimize(oldTour : Tour, newTour : Tour, count : Int) : Tour = {
      if (oldTour.fitness <= newTour.fitness || count == 0) {
        oldTour
      } else {
        optimize(newTour, twoOpt(newTour), count -1)
      }
    }
    optimize(this, twoOpt(this), 1)
  }
}