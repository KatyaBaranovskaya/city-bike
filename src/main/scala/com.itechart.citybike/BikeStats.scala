package com.itechart.citybike

import com.itechart.citybike.parser.BikeTrip
import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object BikeStats extends Logging {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val SOURCE_DIR = config.getString("source-directory")

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val files = reader.getListOfFiles(s"/$SOURCE_DIR")
    val data = files.map(file => reader.readFile(s"$SOURCE_DIR/$file").drop(1).map(parseLine).filter(_.isRight).map(_.right.get))

    val countBikeStatsFutureSeq: List[Future[Seq[(Int, (Int, Int))]]] = data.map(countBikeStats)
    val result: Future[Map[Int, (Int, Int)]] = Future.sequence(countBikeStatsFutureSeq).map(result => {
      result.flatten.groupBy(_._1).mapValues(x => (x.map(_._2._1).sum, x.map(_._2._2).sum))
    })
    result
  }

  def countBikeStats(data: List[BikeTrip]): Future[Seq[(Int, (Int, Int))]] = Future {
    data.groupBy(_.bikeId).mapValues(x => (x.size, x.map(_.tripDuration).sum)).toSeq.sortBy(_._2._2)(Ordering[Int].reverse)
  }
}
