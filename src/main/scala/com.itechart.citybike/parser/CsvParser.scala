package com.itechart.citybike.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.logging.log4j.scala.Logging

object CsvParser extends Logging {

  private val config: Config = ConfigFactory.load("lightbend.conf")

  private val DELIMITER = config.getString("delimiter")

  def parseLine(line: String): Either[String, BikeTrip] = {
    if (line.isEmpty) {
      logger.error("Empty line cannot be parsed")
      return Left("Empty line cannot be parsed")
    }

    try {
      val cols = line.split(DELIMITER).map(_.replace("\"", ""))
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

      Right(bikeTrip)
    } catch {
      case ex: RuntimeException => {
        logger.error("Exception during parsing")
        Left("Exception during parsing")
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
