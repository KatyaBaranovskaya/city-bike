package com.itechart.citybike

import java.time.Month

import com.itechart.citybike.parser.BikeTrip
import com.itechart.citybike.reader.Reader
import com.itechart.citybike.parser.CsvParser.parseLine
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object UsageStats extends Logging {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val SOURCE_DIR = config.getString("source-directory")

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val files = reader.getListOfFiles(s"/$SOURCE_DIR")
    val data = files.map(file => reader.readFile(s"$SOURCE_DIR/$file").drop(1).map(parseLine).filter(_.isRight).map(_.right.get))

    val countTripsByMonthFutureSeq: List[Future[Map[Month, Int]]] = data.map(countTripsByMonth)
    Future.sequence(countTripsByMonthFutureSeq).onComplete {
      case Success(results) => {
        println("res")
        results.foreach(x => print(x))
      }
      case Failure(e) => logger.error("Exception during file processing", e)
    }

    Thread.sleep(300000)
  }

  def countTripsByMonth(data: List[BikeTrip]): Future[Map[Month, Int]] = Future {
    data.groupBy(_.startTime.getMonth).mapValues(_.size)
  }
}
