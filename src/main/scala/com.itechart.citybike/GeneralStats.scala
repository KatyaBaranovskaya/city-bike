package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import com.itechart.citybike.writer.Writer
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GeneralStats extends Logging {

  def main(args: Array[String]): Unit = {
    val startDate = LocalDateTime.parse("2016-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2016-08-03T00:00:00")

    val reader = new Reader()
    val writer = new Writer()
    val files = reader.getListOfFiles("/sources")

    val futureOperations: List[Future[Any]] = files.map(file => getNumberOfTrips(file))
    val futureSequenceResults = Future.sequence(futureOperations)

    futureSequenceResults.onComplete {
      case Success(result) => {
        writer.writeToFile("general-stats.cvs", Seq(result.size.toString))
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    Await.result(futureSequenceResults, Duration.Inf)
  }

  def getNumberOfTrips(fileName: String): Future[Int] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.size
  }

  def getCountLongestTrip(fileName: String): Future[Int] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.map(parseLine).maxBy(_.tripDuration).tripDuration
  }

  def getCountBicycles(fileName: String, startDate: LocalDateTime, endDate: LocalDateTime): Future[Int] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.map(parseLine).filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(endDate)).toTraversable.view.groupBy(_.bikeId).size
  }

  def getCountManPercentage(fileName: String): Future[Double] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.map(parseLine).count(x => x.gender == 1).toDouble
  }

  def getCountWomanPercentage(fileName: String): Future[Double] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.map(parseLine).count(x => x.gender == 2).toDouble
  }
}

//(data.count(x => x.gender == 1).toDouble * 100) / numberOfTrips

//val generalStats = Seq(
//      "Total number of trips - " + numberOfTrips,
//      "The time of the longest trip - " + countLongestTrip(data),
//      "The number of bicycles used during the specified period - " + countBicycles(data, startDate, endDate),
//      "% of men using the service - " + countManPercentage(data, numberOfTrips) + "%",
//      "% of women using the service - " + countWomanPercentage(data, numberOfTrips) + "%",
//      "The number of records that cannot be processed - " + CsvParser.unProcessed,
//    )
