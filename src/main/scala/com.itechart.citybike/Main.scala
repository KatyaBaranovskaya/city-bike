package com.itechart.citybike

import com.itechart.citybike.parser.CsvParser.parseLine
import com.itechart.citybike.reader.Reader
import com.itechart.citybike.writer.Writer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val SOURCE_DIR = config.getString("source-directory")

  def main(args: Array[String]): Unit = {
    val reader = new Reader()
    val writer = new Writer()
    val files = reader.getListOfFiles(s"/$SOURCE_DIR")
    val data = files.map(file => reader.readFile(s"$SOURCE_DIR/$file").drop(1).map(parseLine).filter(_.isRight).map(_.right.get))

    val generalStats = Await.result(GeneralStats.run(data), Duration.Inf)
    writer.writeToFile("general-stats.csv", generalStats)

    val bikeStats = Await.result(BikeStats.run(data), Duration.Inf)
    writer.writeToFile("bike-stats.csv", bikeStats.toSeq)

    val usageStats = Await.result(UsageStats.run(data), Duration.Inf)
    writer.writeToFile("usage-stats.csv", usageStats.toSeq)
  }
}
