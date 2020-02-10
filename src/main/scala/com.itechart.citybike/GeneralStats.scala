package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.{BikeTrip, CsvParser}
import com.itechart.citybike.reader.CsvReader
import com.itechart.citybike.writer.CsvWriter

object GeneralStats {

  def main(args: Array[String]): Unit = {
    val reader = new CsvReader()
    val data = reader.readFile("sources/201608-citibike-tripdata.csv")

    val startDate = LocalDateTime.parse("2016-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2016-08-03T00:00:00")
    val numberOfTrips = data.size

    val generalStats = Seq(
      "Total number of trips - " + numberOfTrips,
      "The time of the longest trip - " + countLongestTrip(data),
      "The number of bicycles used during the specified period - " + countBicycles(data, startDate, endDate),
      "% of men using the service - " + countManPercentage(data, numberOfTrips) + "%",
      "% of women using the service - " + countWomanPercentage(data, numberOfTrips) + "%",
      "The number of records that cannot be processed - " + CsvParser.unProcessed,
    )

    val writer = new CsvWriter()
    writer.writeToFile("general-stats.cvs", generalStats)
  }

  def countLongestTrip(data: Seq[BikeTrip]): Int = data.maxBy(_.tripDuration).tripDuration

  def countBicycles(data: Seq[BikeTrip], startDate: LocalDateTime, endDate: LocalDateTime): Int = data.filter(_.startTime.isAfter(startDate))
    .filter(_.startTime.isBefore(endDate)).groupBy(_.bikeId).size

  def countManPercentage(data: Seq[BikeTrip], numberOfTrips: Int): Double = (data.count(x => x.gender == 1).toDouble * 100) / numberOfTrips

  def countWomanPercentage(data: Seq[BikeTrip], numberOfTrips: Int): Double = (data.count(x => x.gender == 2).toDouble * 100) / numberOfTrips
}

//    val genders = data.groupBy(_.gender).mapValues(_.size)
//    val manPer = genders(1)
//    val womanPer = genders(2)
