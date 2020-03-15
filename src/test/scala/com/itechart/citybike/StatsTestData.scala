package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.BikeTrip

object StatsTestData {

  def bikesData(): List[BikeTrip] = List(
    BikeTrip(492, LocalDateTime.parse("2018-07-03T12:45:30"), LocalDateTime.parse("2018-07-03T17:15:30"), 293, "Lafayette St & E 8 St", 40.73028666, -73.9907647, 466, "W 25 St & 6 Ave", 40.74395411, -73.99144871, 24159, "Subscriber", 1984, 1),
    BikeTrip(130, LocalDateTime.parse("2018-07-04T13:45:30"), LocalDateTime.parse("2018-07-04T15:45:30"), 657, "Lafayette St & E 8 St", 40.73028666, -73.9907647, 466, "W 25 St & 6 Ave", 40.74395411, -73.99144871, 16999, "Subscriber", 1994, 0),
    BikeTrip(478, LocalDateTime.parse("2018-08-12T12:45:30"), LocalDateTime.parse("2018-08-12T16:45:30"), 345, "Lafayette St & E 8 St", 40.73028666, -73.9907647, 466, "W 25 St & 6 Ave", 40.74395411, -73.99144871, 45014, "Subscriber", 1977, 1),
    BikeTrip(905, LocalDateTime.parse("2018-09-01T11:45:30"), LocalDateTime.parse("2018-09-01T12:45:30"), 223, "Lafayette St & E 8 St", 40.73028666, -73.9907647, 466, "W 25 St & 6 Ave", 40.74395411, -73.99144871, 16999, "Subscriber", 1990, 2),
    BikeTrip(309, LocalDateTime.parse("2018-09-13T17:45:30"), LocalDateTime.parse("2018-09-13T19:45:30"), 512, "Lafayette St & E 8 St", 40.73028666, -73.9907647, 466, "W 25 St & 6 Ave", 40.74395411, -73.99144871, 22345, "Subscriber", 1999, 1)
  )
}
