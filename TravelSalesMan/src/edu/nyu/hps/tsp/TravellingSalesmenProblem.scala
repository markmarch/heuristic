package edu.nyu.hps.tsp
import scala.collection.immutable.HashSet

object TravellingSalesmenProblem {
  def main(args : Array[String]) : Unit = {
    import scala.io.Source
    
    val cities = (for(line <- Source.fromFile(args(0)).getLines()) yield createCity(line)).toList
    cities.foreach(println)
  }
  
  def createCity(line : String) = {
    val data = line.split(" ").flatMap((token : String) => Some(token.toInt))
    new City(data(0), data(1), data(2), data(3))
  }
}

class City(val number : Int, val x : Int, val y : Int, val z : Int) {
  val cordinates = List(x, y, z)
  def distanceTo(other : City) = City.distanceBetween(this, other)
  
  override def toString() = {
    number + ": [" + cordinates.mkString(",") + "]"
  }
}

object City {
  def distanceBetween(cityA : City, cityB : City) = {
    Math.sqrt((cityA.cordinates, cityB.cordinates).zipped.map((a, b) => (a - b) * (a - b)).reduceLeft(_ + _))
  }
}

class Edge(val cityA : City, val cityB : City) {
  val weight = cityA.distanceTo(cityB)
}

class Graph(val cities : List[City], val edges : List[Edge]) {

  // get the minimum spanning tree of this graph
  def getMST() = {
    def addEdge(set : Set[City], selectedEdges : List[Edge], edgeList : List[Edge]) : List[Edge] = {
      if (set.size == cities.length) selectedEdges
      else {
        edgeList match {
          case xs :: ls if !set.contains(xs.cityA) && !set.contains(xs.cityB) => xs :: edgeList
          case xs :: ls => addEdge(set, selectedEdges, ls)
          case Nil => selectedEdges
        }
      }
    }

    val selectedEdges = addEdge(new HashSet[City], List(), edges.sortWith((e1, e2) => if (e1.weight < e2.weight) true else false))
    new Graph(cities, selectedEdges)
  }
}