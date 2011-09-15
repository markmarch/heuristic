package edu.nyu.hps.mint

import scala.collection.mutable.ListBuffer

object ExactChangeNumber {
	def main(args: Array[String]) {
	  val (denomination, score) = findDenomination(1.0)
	  
	  println("------Optimal Solution-------")
	  print(denomination + ":" + score)
	}
	
	def findDenomination(n: Double) = {
	  var denomination = List[Int]()
	  var score = Double.MaxValue
	  var count = 0
	  for {
	      i <- 2 to 96
	      j <- i+1 to 97
	      m <- j+1 to 98
	      k <- m+1 to 99 } 
	  {
	        val d = List(1, i, j, m, k)
	        val (entries, _) = findOptimal(d)
	        val temp = calculateScore(n, entries)
	        println(d + ":" + temp)
	        if (score > temp) {
	          score = temp
	          denomination = d
	        }
	  }
	  (denomination, score)    
	}
	
	def calculateScore(n:Double, entries:List[Int]) = {
	  var score = 0.0
	  for (i <- 0 until entries.length) {
	    if ((i + 1) % 5 == 0) score += n * entries(i) else score += entries(i)
	  }
	  score
	}
	
	def printExchange(price:Int, exchange:List[Int]) {
	  println(price + (" " * (2 - price.toString.length)) + " [" + exchange.foldLeft("")((a,b)=>a + " " + b)
	      + "]\t" + exchange.foldLeft(0)((a,b)=> a+b) )
	}
	
	def findOptimal(d: List[Int]) = {
	  val m = new Array[Int](100)
	  val partition = new Array[Int](100)
	  val result = new Array[List[Int]](100)
	  result(0) = List(0, 0, 0, 0, 0)
	  for (i <- 1 to 99) {
	    if(d.contains(i)) {
	      m(i) = 1
	      result(i) = makeChange(d.indexOf(i), d.length)
	    } else {
	      m(i) = Int.MaxValue
	      result(i) = result(0)
	    }
	  }
	  
	  for {
	    i<- 1 to 99
	    j<- 1 to i/2
	    if (m(i) > m(i-j) + m(j))
	  } {
	    m(i) = m(i-j) + m(j)
	    partition(i) = j
	  }

	  for(i<-1 until result.length) 
	    result(i) = List.map2(result(partition(i)), result(i - partition(i)))(_ + _)
	  (m toList, result toList)
	}
	
	def makeChange(index:Int, length:Int) = {
	  val change = new Array[Int](length)
	  change(index) = 1
	  change toList
	}
}

class ExactExchangeNumber(val denomination:List[Int]) {
  var changeNumber = Map[Int, List[Int]]()
  
  def calculateScore(n: Double) {
    
  }
  
  private def findExactExchangeNumber(price: Int) {
    
  }
}