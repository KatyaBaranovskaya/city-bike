package com.itechart.citybike.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.itechart.citybike.BikeTrip

object CsvParser {

  def parseLine(line: String): BikeTrip = {
    val formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss")
    val cols = line.split(",").map(_.replace("\"", ""))

    val bikeTrip = new BikeTrip()
    bikeTrip.tripDuration = cols(0).toInt
    bikeTrip.startTime = LocalDateTime.parse(cols(1), formatter)
    bikeTrip.stopTime = LocalDateTime.parse(cols(2), formatter)
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
