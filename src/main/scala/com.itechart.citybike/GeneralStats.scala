package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import com.itechart.citybike.writer.Writer
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object GeneralStats extends Logging {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val SOURCE_DIR = config.getString("source-directory")

  private val reader = new Reader()

  def main(args: Array[String]): Unit = {
    val files = reader.getListOfFiles("/" + SOURCE_DIR)

    val countTripsFutureSeq: List[Future[Int]] = files.map(file => countTrips(file))
    Future.sequence(countTripsFutureSeq).onComplete {
        case Success(result) => {
          println("Total number of trips: " + result.sum)
        }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countLongestTripFutureSeq: List[Future[Int]] = files.map(file => countLongestTrip(file))
    Future.sequence(countLongestTripFutureSeq).onComplete {
      case Success(result) => {
        println("The time of the longest trip: " + result.max)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val startDate = LocalDateTime.parse("2015-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2017-08-03T00:00:00")
    val countBicyclesFutureSeq: List[Future[Set[Int]]] = files.map(file => countBicycles(file, startDate, endDate))
    Future.sequence(countBicyclesFutureSeq).onComplete {
      case Success(result) => {
        println("The number of bicycles used during the specified period: " + result.flatten.toSet.size)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countManFutureSeq: List[Future[Int]] = files.map(file => countMan(file))
    Future.sequence(countManFutureSeq).onComplete {
      case Success(result) => {
        println("The number of men using the service: " + result.sum)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countWomanFutureSeq: List[Future[Int]] = files.map(file => countWoman(file))
    Future.sequence(countWomanFutureSeq).onComplete {
      case Success(result) => {
        println("The number of women using the service: " + result.sum)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    Thread.sleep(300000)
  }

  def countTrips(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.size
  }

  def countLongestTrip(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).maxBy(_.tripDuration).tripDuration
  }

  def countBicycles(fileName: String, startDate: LocalDateTime, endDate: LocalDateTime): Future[Set[Int]] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(endDate)).map(x => x.bikeId).toSet
  }

  def countMan(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).count(x => x.gender == 1)
  }

  def countWoman(fileName: String): Future[Int] = Future {
    val data = reader.readFile(SOURCE_DIR + "/" + fileName).drop(1)
    data.map(parseLine).count(x => x.gender == 2)
  }
}

//val generalStats = Seq(
//      "Total number of trips - " + numberOfTrips,
//      "The time of the longest trip - " + countLongestTrip(data),
//      "The number of bicycles used during the specified period - " + countBicycles(data, startDate, endDate),
//      "% of men using the service - " + countManPercentage(data, numberOfTrips) + "%",
//      "% of women using the service - " + countWomanPercentage(data, numberOfTrips) + "%",
//      "The number of records that cannot be processed - " + CsvParser.unProcessed,
//    )
