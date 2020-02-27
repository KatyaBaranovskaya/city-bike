package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import com.itechart.citybike.writer.Writer
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GeneralStats extends Logging {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val SOURCE_DIR = config.getString("source-directory")

  private val reader = new Reader()

  def main(args: Array[String]): Unit = {
    val files = reader.getListOfFiles("/" + SOURCE_DIR)

    val startDate = LocalDateTime.parse("2015-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2017-08-03T00:00:00")

    val futureOperations: List[Future[Int]] = files.map(file => getNumberOfTrips(file))
    val futureSequenceResults = Future.sequence(futureOperations)

    val futureOperations2: List[Future[Int]] = files.map(file => getCountLongestTrip(file))
    val futureSequenceResults2 = Future.sequence(futureOperations2)

    futureSequenceResults.onComplete {
        case Success(result) => {
          println("result: " + result.sum)
        }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    futureSequenceResults2.onComplete {
      case Success(result) => {
        println("result2: " + result.max)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

//    val res = Await.result(futureSequenceResults, Duration.Inf)

    Thread.sleep(300000)
//    println(s"Future val is: ${res.flatten.toSet.size}")
  }

  def getNumberOfTrips(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.size
  }

  def getCountLongestTrip(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).maxBy(_.tripDuration).tripDuration
  }

  def getCountBicycles(fileName: String, startDate: LocalDateTime, endDate: LocalDateTime): Future[Set[Int]] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(endDate)).map(x => x.bikeId).toSet
  }

  def getCountManPercentage(fileName: String): Future[Double] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).count(x => x.gender == 1).toDouble
  }

  def getCountWomanPercentage(fileName: String): Future[Double] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
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
