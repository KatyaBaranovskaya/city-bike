package com.itechart.citybike

import java.time.Month

import com.itechart.citybike.reader.Reader
import com.itechart.citybike.parser.CsvParser.parseLine
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object UsageStats extends Logging {

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val files = reader.getListOfFiles("/sources")

    val futureOperations: List[Future[Any]] = files.map(file => getTripsByMonth(file))
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

  def getTripsByMonth(fileName: String): Future[Map[Month, Int]] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.map(parseLine).toStream.groupBy(_.startTime.getMonth).mapValues(_.size)
  }
}
