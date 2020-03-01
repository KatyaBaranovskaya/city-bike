package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.BikeTrip
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

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val files = reader.getListOfFiles(s"/$SOURCE_DIR")
    val data = files.map(file => reader.readFile(s"$SOURCE_DIR/$file").drop(1).map(parseLine).filter(_.isRight).map(_.right.get))

    val countTripsFutureSeq: List[Future[Int]] = data.map(countTrips)
    Future.sequence(countTripsFutureSeq).onComplete {
        case Success(result) => {
          println("Total number of trips: " + result.sum)
        }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countLongestTripFutureSeq: List[Future[Int]] = data.map(countLongestTrip)
    Future.sequence(countLongestTripFutureSeq).onComplete {
      case Success(result) => {
        println("The time of the longest trip: " + result.max)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val startDate = LocalDateTime.parse("2015-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2017-08-03T00:00:00")
    val countBicyclesFutureSeq: List[Future[Set[Int]]] = data.map(countBicycles(_, startDate, endDate))
    Future.sequence(countBicyclesFutureSeq).onComplete {
      case Success(result) => {
        println("The number of bicycles used during the specified period: " + result.flatten.toSet.size)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countManFutureSeq: List[Future[Int]] = data.map(countMan)
    Future.sequence(countManFutureSeq).onComplete {
      case Success(result) => {
        println("The number of men using the service: " + result.sum)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    val countWomanFutureSeq: List[Future[Int]] = data.map(countWoman)
    Future.sequence(countWomanFutureSeq).onComplete {
      case Success(result) => {
        println("The number of women using the service: " + result.sum)
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    Thread.sleep(300000)
  }

  def countTrips(data: List[BikeTrip]): Future[Int] = Future {
    data.size
  }

  def countLongestTrip(data: List[BikeTrip]): Future[Int] = Future {
    data.maxBy(_.tripDuration).tripDuration
  }

  def countBicycles(data: List[BikeTrip], startDate: LocalDateTime, endDate: LocalDateTime): Future[Set[Int]] = Future {
    data.filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(endDate)).map(x => x.bikeId).toSet
  }

  def countMan(data: List[BikeTrip]): Future[Int] = Future {
    data.count(x => x.gender == 1)
  }

  def countWoman(data: List[BikeTrip]): Future[Int] = Future {
    data.count(x => x.gender == 2)
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
