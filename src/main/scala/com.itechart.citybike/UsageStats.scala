package com.itechart.citybike

import java.time.Month

import com.itechart.citybike.parser.BikeTrip
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UsageStats extends Logging {

  def run(data: List[List[BikeTrip]]): Future[Map[Month, Int]] = {
    val countTripsByMonthFutureSeq: List[Future[Map[Month, Int]]] = data.map(countTripsByMonth)
    val result = Future.sequence(countTripsByMonthFutureSeq).map(result => {
      result.flatten.groupBy(_._1).mapValues(_.map(_._2).sum)
    })
    result
  }

  def countTripsByMonth(data: List[BikeTrip]): Future[Map[Month, Int]] = Future {
    data.groupBy(_.startTime.getMonth).mapValues(_.size)
  }
}
