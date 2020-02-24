package com.itechart.citybike

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object BikeStats extends Logging {

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val files = reader.getListOfFiles("/sources")

    val futureOperations: List[Future[Any]] = files.map(file => getBikeStats(file))
    val futureSequenceResults = Future.sequence(futureOperations)

    futureSequenceResults.onComplete {
      case Success(results) => {
        println("res")
        results.foreach(x => print(x))
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    Await.result(futureSequenceResults, Duration.Inf)
  }

  def getBikeStats(fileName: String): Future[Seq[(Int, (Int, Int))]] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.toStream.map(parseLine).groupBy(_.bikeId).mapValues(x => (x.size, x.map(_.tripDuration).sum)).toSeq.sortBy(_._2._2)(Ordering[Int].reverse)
  }
}
