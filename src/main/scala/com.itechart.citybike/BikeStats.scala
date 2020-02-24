package com.itechart.citybike

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object BikeStats {

  def main(args: Array[String]): Unit = {
    val reader = new CsvReader()
    val data = reader.readFile("sources/201608-citibike-tripdata.csv")

    val bikeStats = getBikeStats(data).map(x => x._1 + " - " + x._2._1 + " - " + x._2._2)

    val writer = new CsvWriter()
    writer.writeToFile("bike-stats.cvs", bikeStats)
  }

  def getBikeStats(fileName: String): Future[Seq[(Int, (Int, Int))]] = Future {
    val reader = new Reader()
    val data = reader.readFile("sources" + "/" + fileName).drop(1)
    data.toStream.map(parseLine).groupBy(_.bikeId).mapValues(x => (x.size, x.map(_.tripDuration).sum)).toSeq.sortBy(_._2._2)(Ordering[Int].reverse)
  }
}
