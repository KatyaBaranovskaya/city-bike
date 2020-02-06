package com.itechart.citybike

import java.io.{File, PrintWriter}
import java.time.LocalDateTime

import scala.io.Source

object Main {

  def main(args: Array[String]): Unit = {
    val list = readFile("sources/201608-citibike-tripdata.csv")

    val numberOfTrips = list.size
    val longestTrip = list.maxBy(_.tripDuration).tripDuration

    //    val genders = list.groupBy(_.gender).mapValues(_.size)
    //    val men = genders(1)
    //    val womanPer = genders(2)
    val manPer = (list.count(x => x.gender == 1).toDouble * 100) / numberOfTrips
    val womanPer = (list.count(x => x.gender == 2).toDouble * 100) / numberOfTrips

    writeToFile("general-stats.cvs", Seq(numberOfTrips, longestTrip, manPer, womanPer))
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

  def parseLine(line: String): BikeTrip = {
    val cols = line.split(",").map(_.replace("\"", ""))
    val bikeTrip = new BikeTrip()
    bikeTrip.tripDuration = cols(0).toInt
    //    bikeTrip.startTime = LocalDateTime.parse(cols(1))
    //    bikeTrip.stopTime = LocalDateTime.parse(cols(2))
    bikeTrip.startStationId = cols(3).toInt
    bikeTrip.startStationName = cols(4)
    bikeTrip.startStationLatitude = cols(5).toDouble
    bikeTrip.startStationLongitude = cols(6).toDouble
    bikeTrip.endStationId = cols(7).toInt
    bikeTrip.endStationName = cols(8)
    bikeTrip.endStationLatitude = cols(9).toDouble
    bikeTrip.endStationLongitude = cols(10).toDouble
    bikeTrip.bikeId = cols(11).toInt
    bikeTrip.userType = cols(12)
    //    bikeTrip.birthYear = cols(13).toInt
    bikeTrip.gender = cols(14).toInt

    bikeTrip
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
