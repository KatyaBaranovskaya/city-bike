package com.itechart.citybike

import java.time.LocalDateTime

import com.itechart.citybike.parser.BikeTrip
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object GeneralStats extends Logging {

  def run(data: List[List[BikeTrip]]): Future[List[AnyVal]] = {
    val countTripsFutureSeq: List[Future[Int]] = data.map(countTrips)
    val trips: Future[Int] = Future.sequence(countTripsFutureSeq).map(result => {
      result.sum
    })

    val countLongestTripFutureSeq: List[Future[Int]] = data.map(countLongestTrip)
    val longestTrip: Future[Int] = Future.sequence(countLongestTripFutureSeq).map(result => {
      result.max
    })

    val startDate = LocalDateTime.parse("2015-08-01T00:00:00")
    val endDate = LocalDateTime.parse("2017-08-03T00:00:00")
    val countBicyclesFutureSeq: List[Future[Set[Int]]] = data.map(countBicycles(_, startDate, endDate))
    val bicycles: Future[Int] = Future.sequence(countBicyclesFutureSeq).map(result => {
      result.flatten.toSet.size
    })

    val countManFutureSeq: List[Future[Int]] = data.map(countMan)
    val mans: Future[Int] = Future.sequence(countManFutureSeq).map(result => {
      result.sum
    })

    val countWomanFutureSeq: List[Future[Int]] = data.map(countWoman)
    val womans: Future[Int] = Future.sequence(countWomanFutureSeq).map(result => {
      result.sum
    })

    val manPer: Future[Double] = trips.flatMap(res => {
      mans.map(res2 => {
        res2.toDouble * 100 / res
      })
    })

    val womanPer: Future[Double] = trips.flatMap(res => {
      womans.map(res2 => {
        res2.toDouble * 100 / res
      })
    })

    val list: List[Future[AnyVal]] = List(trips, longestTrip, bicycles, manPer, womanPer)
    Future.sequence(list)
  }

  def countTrips(data: List[BikeTrip]): Future[Int] = Future {
    data.size
  }

  def countLongestTrip(data: List[BikeTrip]): Future[Int] = Future {
    data.maxBy(_.tripDuration).tripDuration
  }

  def countBicycles(data: List[BikeTrip], startDate: LocalDateTime, endDate: LocalDateTime): Future[Set[Int]] = Future {
    data.filter(_.startTime.isAfter(startDate)).filter(_.startTime.isBefore(endDate)).map(x => x.bikeId).toSet
  }

  def countMan(data: List[BikeTrip]): Future[Int] = Future {
    data.count(x => x.gender == 1)
  }

  def countWoman(data: List[BikeTrip]): Future[Int] = Future {
    data.count(x => x.gender == 2)
  }
}
