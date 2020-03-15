package com.itechart.citybike.stats

import com.itechart.citybike.{BikeStats, StatsTestData}
import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class BikeStatsSpec extends AsyncFlatSpec {

  it should "count a bikes stats" in {
    val futureBikeStats: Future[Seq[(Int, (Int, Int))]] = BikeStats.countBikeStats(StatsTestData.bikesData())
    val result: Seq[(Int, (Int, Int))] = List(
      (16999, (2, 1035)),
      (24159, (1, 492)),
      (45014, (1, 478)),
      (22345, (1, 309)),
    )

    futureBikeStats map { res => assert(res == result) }
  }
}
