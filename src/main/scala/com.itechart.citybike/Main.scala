package com.itechart.citybike

import java.io.{File, PrintWriter}
import java.time.LocalDateTime

import com.itechart.citybike.parser.CsvParser.parseLine

import scala.io.Source

object Main {

  def main(args: Array[String]): Unit = {
    val list = readFile("sources/201608-citibike-tripdata.csv")

    val startDate = LocalDateTime.parse("2016-08-01T00:00:00")
    val stopDate = LocalDateTime.parse("2016-08-03T00:00:00")

    val numberOfTrips = list.size
    val longestTrip = list.maxBy(_.tripDuration).tripDuration
    val bikes = list.filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(stopDate)).groupBy(_.bikeId).size

    //    val genders = list.groupBy(_.gender).mapValues(_.size)
    //    val men = genders(1)
    //    val womanPer = genders(2)
    val manPer = (list.count(x => x.gender == 1).toDouble * 100) / numberOfTrips
    val womanPer = (list.count(x => x.gender == 2).toDouble * 100) / numberOfTrips

    writeToFile("general-stats.cvs", Seq(numberOfTrips, longestTrip, bikes, manPer, womanPer))
  }

  def readFile(fileName: String) = {
    val source = Source.fromResource(fileName)
    val lineIterator = source.getLines()

    val result = lineIterator.drop(1).map(parseLine).toList

    source.close()
    result
  }

  def writeToFile(fileName: String, values: Seq[Any]) = {
    val pw = new PrintWriter(new File(fileName))
    values.foreach(pw.println)

    pw.close()
  }
}

class BikeTrip {
  var tripDuration: Int = 0
  var startTime: LocalDateTime = _
  var stopTime: LocalDateTime = _
  var startStationId: Int = 0
  var startStationName: String = _
  var startStationLatitude: Double = _
  var startStationLongitude: Double = _
  var endStationId: Int = 0
  var endStationName: String = _
  var endStationLatitude: Double = _
  var endStationLongitude: Double = _
  var bikeId: Int = 0
  var userType: String = _
  var birthYear: Int = 0
  var gender: Int = 0
}
