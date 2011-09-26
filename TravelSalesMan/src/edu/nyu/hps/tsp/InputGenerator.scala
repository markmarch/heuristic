package edu.nyu.hps.tsp
import java.io.PrintWriter
import java.io.File

object InputGenerator {
  def main(args : Array[String]) : Unit = {
    generateInput(1000, "sample.txt")
  }
  def generateInput(size : Int, fileName : String) = {
    import scala.util.Random
    val maxX = 2500
    val maxY = 2500
    val maxZ = 500
    writeToFile(new File(fileName))((p => {
      for(i <- 1 to size) 
      p.println( i + " " + (Random.nextInt(maxX) + 1) + " " + (Random.nextInt(maxY) + 1) + 
      " " + (Random.nextInt(maxZ) + 1))
    }))
  }
  
  def writeToFile(file : File)(op : PrintWriter => Unit) {
    val printer = new PrintWriter(file)
    try {
      op(printer)
    } finally {
      printer.close
    }
  }
}