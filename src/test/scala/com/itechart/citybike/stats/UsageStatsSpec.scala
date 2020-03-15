package com.itechart.citybike.stats

import java.time.Month

import com.itechart.citybike.{StatsTestData, UsageStats}
import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class UsageStatsSpec extends AsyncFlatSpec {

  it should "count a usage stats" in {
    val futureBikeStats: Future[Map[Month, Int]] = UsageStats.countTripsByMonth(StatsTestData.bikesData())
    val result: Map[Month, Int] = Map(
      Month.JULY -> 2,
      Month.SEPTEMBER -> 2,
      Month.AUGUST -> 1
    )

    futureBikeStats map { res => assert(res == result) }
  }
}
