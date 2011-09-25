//package edu.nyu.hps.tsp
//
//object TSPAlgorithm {
//
//  // Find a tour using nearest insertion algorithm
//  def nearestInsertion(graph : Graph) = {
//    val sortedEdges = graph.edges.sortBy(_.weight)
//    val shortestEdge = sortedEdges.head
//    val subTour = new Tour(Set(shortestEdge.vertexA, shortestEdge.vertexB), Set(shortestEdge))
//
//    def findNearestVertex(sub : List[Vertex], rest : List[Vertex]) = {
//      var nearest = sub(0) distanceTo rest(0)
//      var selected = rest(0)
//      for (v <- sub; u <- rest) {
//        val temp = v distanceTo u
//        if (temp < nearest) {
//          nearest = temp
//          selected = u
//        }
//      }
//      selected
//    }
//
//    def findOptimalEdge(v : Vertex, edges : List[Edge], cost : Double, minCost : Double, edge : Edge) : Edge = {
//      edges match {
//        case Nil => edge
//        case e :: ls => {
//          val newCost = cost + replaceCost(v, e)
//          if (newCost < minCost)
//            findOptimalEdge(v, ls, cost, newCost, e)
//          else
//            findOptimalEdge(v, ls, cost, minCost, edge)
//        }
//      }
//    }
//
//    // cost increased to replace edge e wigh two new edges (e.vertexA, v) and
//    // (v, e.vertextB)
//    def replaceCost(v: Vertex, e :Edge) = {
//      v.distanceTo(e.vertexA) + v.distanceTo(e.vertexB) - e.weight
//    }
//    def findTour(subTour : Tour, rest : Set[Vertex]) : Tour = {
//      rest match {
//        case e if e.isEmpty => subTour
//        case _ => {
//          val v = findNearestVertex(subTour.vertices.toList, rest.toList)
//          val edge = new Edge(subTour.vertices.first, subTour.vertices.tail.first)
//          val minCost = subTour.cost + replaceCost(v, edge)
//          val optimalEdge = findOptimalEdge(v, subTour.edges.toList, subTour.cost, minCost,edge)
//          findTour(new Tour(subTour.vertices + v, subTour.edges + optimalEdge), rest - v)
//        }
//      }
//    }
//    
//    val tour = findTour(subTour, graph.vertices.toSet -- subTour.vertices)
//    tour.edges.toList
//  }
//}