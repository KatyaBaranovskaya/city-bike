package com.itechart.citybike.stats

import java.time.LocalDateTime

import com.itechart.citybike.{GeneralStats, StatsTestData}
import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class GeneralStatsSpec extends AsyncFlatSpec {

  it should "count a number of trips" in {
    val futureTripNum: Future[Int] = GeneralStats.countTrips(StatsTestData.bikesData())

    futureTripNum map { num => assert(num == 5) }
  }

  it should "count a longest trip" in {
    val futureLongestTrip: Future[Int] = GeneralStats.countLongestTrip(StatsTestData.bikesData())

    futureLongestTrip map { res => assert(res == 905) }
  }

  it should "count a bicycles in period" in {
    val startDate = LocalDateTime.parse("2018-07-01T00:00:00")
    val endDate = LocalDateTime.parse("2018-09-03T00:00:00")
    val futureBicyclesNum: Future[Set[Int]] = GeneralStats.countBicycles(StatsTestData.bikesData(), startDate, endDate)

    futureBicyclesNum map { num => assert(num.size == 3) }
  }

  it should "count a number of mans" in {
    val futureManNum: Future[Int] = GeneralStats.countMan(StatsTestData.bikesData())

    futureManNum map { num => assert(num == 3) }
  }

  it should "count a number of womans" in {
    val futureWomanNum: Future[Int] = GeneralStats.countWoman(StatsTestData.bikesData())

    futureWomanNum map { num => assert(num == 1) }
  }
}
