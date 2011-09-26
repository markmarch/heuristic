package edu.nyu.hps.tsp

import scala.annotation.tailrec

class Population(tours : List[Tour], generationSize : Int, selection : Double, mutationRate : Double) {
  val populationSize = tours.length
  val eliminationSize = (populationSize * selection).toInt
  val mutationSize = (populationSize * mutationRate).toInt

  def findOptimal() = {
    evolve(tours, generationSize)
  }

  @tailrec private def evolve(individuls : List[Tour], count : Int) : Tour = {
    println("Generation " + (generationSize - count))
    if (count == 0) {
      individuls.head
    } else {
      evolve(createNewPopulation(individuls), count - 1)
    }
  }

  private def createNewPopulation(individuals : List[Tour]) = {
    
    // natural selection
    @tailrec def eliminate(list : List[Tour], count : Int) : List[Tour] = {
      if (count == 0)
        list
      else {
        val r = random.nextInt(list.length)
        eliminate(list.take(r) ::: list.drop(r + 1), count - 1)
      }
    }

    val afterSelection = eliminate(individuals, eliminationSize)
    println("natural selection finished.")
    
    def multiplication(list : List[Tour]) = {
      val totalFitness = list.map(_.fitness).sum
      val chance = list.map((t) => (populationSize * (1 - t.fitness / totalFitness)).toInt)
      val pool = shuffle(chance.zipWithIndex.flatMap(((p)) => Array.fill(p._1)(p._2)))
      
      @tailrec def addToPopulation(population : List[Tour], count : Int) : List[Tour] = {
        if (count == 0)
          population
        else {
          val father = list(pool(random.nextInt(pool.length)))
          val mother = list(pool(random.nextInt(pool.length)))
          addToPopulation(father.crossover(mother) :: population, count - 1)
        }
      }

      addToPopulation(list, populationSize - afterSelection.size)
    }

    val afterMultiplication = multiplication(afterSelection.sortBy(_.fitness))
    println("multiplication finished")
    
    @tailrec def mutation(list : List[Tour], count : Int) : List[Tour] = {
      println("mutating #" + count)
      if (count == 0)
        list
      else {
        val r = count
        mutation(list.take( - 1) ::: List(afterMultiplication(r).mutate()) ::: list.drop(r), count - 1)
      }
    }

    val result = mutation(afterMultiplication, mutationSize)
    println("mutation finished")
    result
  }
}

object Population {
  // randomly generate the initial population
  def generate(size : Int, map : Array[Array[Double]]) = {
    (for (i <- 1 to size) yield new Tour(shuffle((0 to map.length - 1).toList), map)).toList
  }
}