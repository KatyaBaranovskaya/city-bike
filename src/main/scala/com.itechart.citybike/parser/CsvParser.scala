package com.itechart.citybike.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.apache.logging.log4j.scala.Logging

object CsvParser extends Logging {
  var unProcessed = 0

  def parseLine(line: String): BikeTrip = {
    if (line.isEmpty) {
      unProcessed += 1
      logger.error("Empty line cannot be parsed")
      throw new RuntimeException("Empty line cannot be parsed")
    }

    try {
      val cols = line.split(",").map(_.replace("\"", ""))
      val bikeTrip = BikeTrip(
        getInt(cols(0)),
        getDate(cols(1)),
        getDate(cols(2)),
        getInt(cols(3)),
        cols(4),
        getDouble(cols(5)),
        getDouble(cols(6)),
        getInt(cols(7)),
        cols(8),
        getDouble(cols(9)),
        getDouble(cols(10)),
        getInt(cols(11)),
        cols(12),
        getInt(cols(13)),
        getInt(cols(14))
      )

      bikeTrip
    } catch {
      case e: RuntimeException => {
        unProcessed += 1
        logger.error("Exception during parsing")
        throw new RuntimeException("Exception during parsing", e)
      }
    }
  }

  def getInt(value: String): Int = {
    if (!value.isEmpty) {
      value.toInt
    } else 0
  }

  def getDouble(value: String): Double = {
    if (!value.isEmpty) {
      value.toDouble
    } else 0
  }

  def getDate(value: String): LocalDateTime = {
    val formatter = DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss")

    if (!value.isEmpty) {
      LocalDateTime.parse(value, formatter)
    } else null
  }
}

case class BikeTrip(tripDuration: Int, startTime: LocalDateTime, stopTime: LocalDateTime, startStationId: Int,
                    startStationName: String, startStationLatitude: Double, startStationLongitude: Double,
                    endStationId: Int, endStationName: String, endStationLatitude: Double, endStationLongitude: Double,
                    bikeId: Int, userType: String, birthYear: Int, gender: Int)
