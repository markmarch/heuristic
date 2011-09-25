//package edu.nyu.hps.tsp
//import scala.collection.immutable.HashSet
//import scala.collection.immutable.TreeMap
//
//object TravellingSalesmenProblem {
//  def main(args : Array[String]) : Unit = {
//    import scala.io.Source
//
//    val vertices = (for (line <- Source.fromFile(args(0)).getLines())
//      yield createVertex(line)).toList
//
//    val edges = for {
//      i <- 0 until vertices.length;
//      j <- i + 1 until vertices.length
//    } yield new Edge(vertices(i), vertices(j))
//
//    val graph = new Graph(vertices, edges.toList);
//    val selectedEdges = TSPAlgorithm.nearestInsertion(graph)
//    selectedEdges.foreach(println)
//
//    //    val mst = graph.getMST()
//    //
//    //    mst.edges.foreach((edge) => println(edge.vertexA.number + " - " + edge.vertexB.number + " : " + edge.weight))
//    //    val groupedEdges = mst.edges.groupBy {
//    //      case edge => edge.vertexA.number
//    //    }
//    //    val tmp = groupedEdges.toSeq.sortBy(_._1)
//    //    tmp.foreach((a) => println(a._1 + ":" + a._2.map(_.vertexB.number).mkString(",")))
//    //    TreeMap(groupedEdges.toSeq:_*).foreach((x) => println(x._1 + ": " + (x._2).map(_.cityB.number).mkString(",")))
//  }
//
//
//  // create a City object with given data as "city_number x y z"
//  def createVertex(line : String) = {
//    val data = line.split(" ").flatMap((token : String) => Some(token.toInt))
//    new Vertex(data(0), data(1), data(2), data(3))
//  }
//
//  // time how long a call to function f takes
//  def time(f : => Unit) = {
//    val start = System.currentTimeMillis
//    f
//    val timeUsed = System.currentTimeMillis - start
//    println("Time used: " + timeUsed + " ms")
//  }
//}
//
//class Vertex(val number : Int, val x : Int, val y : Int, val z : Int) {
//  val cordinates = List(x, y, z)
//  def distanceTo(other : Vertex) = Vertex.distanceBetween(this, other)
//
//  override def toString() = {
//    number + ": [" + cordinates.mkString(",") + "]"
//  }
//}
//
//object Vertex {
//  // calculate distance between 2 cities
//  def distanceBetween(vA : Vertex, vB : Vertex) = {
//    Math.sqrt((vA.cordinates, vB.cordinates).zipped.map((a, b) => (a - b) * (a - b)).sum)
//  }
//}
//
//class Edge(val vertexA : Vertex, val vertexB : Vertex) {
//  val weight = vertexA.distanceTo(vertexB)
//
//  override def toString = vertexA.number + " --- " + vertexB.number + ", " + weight
//}
//
//class Graph(val vertices : List[Vertex], val edges : List[Edge]) {
//  // get the minimum spanning tree of this graph
//  def getMST() = {
//    // continuous add the cheapest edge to selected edge list to form
//    // minimum spanning tree. Kruskal's Algorithm.
//    def addEdge(set : Set[Vertex], selectedEdges : List[Edge], edgeList : List[Edge]) : List[Edge] = {
//      if (set.size == vertices.length)
//        selectedEdges
//      else {
//        edgeList match {
//          case xs :: ls if !set.contains(xs.vertexA) || !set.contains(xs.vertexB) => {
//            addEdge(set + xs.vertexA + xs.vertexB, xs :: selectedEdges, ls)
//          }
//          case xs :: ls => addEdge(set, selectedEdges, ls)
//          case Nil => selectedEdges
//        }
//      }
//    }
//    val selectedEdges = addEdge(new HashSet[Vertex], List(),
//      edges.sortBy(_.weight))
//    new Graph(vertices, selectedEdges)
//  }
//}
//
//class Tour(val vertices : Set[Vertex], val edges : Set[Edge]) {
//  val cost = edges.foldLeft(0.0)((sum, edge) => sum + edge.weight)
//}