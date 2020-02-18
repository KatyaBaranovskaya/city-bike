package com.itechart.citybike

import com.itechart.citybike.parser.BikeTrip
import com.itechart.citybike.reader.CsvReader
import com.itechart.citybike.writer.CsvWriter

object BikeStats {

  def main(args: Array[String]): Unit = {
    val reader = new CsvReader()
    val data = reader.readFile("sources/201608-citibike-tripdata.csv")

    val bikeStats = getBikeStats(data).map(x => x._1 + " - " + x._2._1 + " - " + x._2._2)

    val writer = new CsvWriter()
    writer.writeToFile("bike-stats.cvs", bikeStats)
  }

  def getBikeStats(data: Seq[BikeTrip]): Seq[(Int, (Int, Int))] = data.groupBy(_.bikeId).mapValues(x => (x.size, x.map(_.tripDuration).sum)).toSeq.sortBy(_._2._2)(Ordering[Int].reverse)
}
