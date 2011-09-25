package edu.nyu.hps.tsp

class Link(val from : Int, val to : Int) {
  override def toString = from + "---" + to
}