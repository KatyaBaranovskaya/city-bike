package com.itechart.citybike

import java.time.Month

import com.itechart.citybike.parser.BikeTrip
import com.itechart.citybike.reader.CsvReader
import com.itechart.citybike.writer.CsvWriter

object UsageStats {

  def main(args: Array[String]): Unit = {
    val reader = new CsvReader()
    val data = reader.readFile("sources/201608-citibike-tripdata.csv")

    val usageStats = getTripsByMonth(data).map(x => x._1 + " - " + x._2).toSeq

    val writer = new CsvWriter()
    writer.writeToFile("usage-stats.cvs", usageStats)
  }

  def getTripsByMonth(data: Seq[BikeTrip]): Map[Month, Int] = data.groupBy(_.startTime.getMonth).mapValues(_.size)
}
