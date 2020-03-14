package com.itechart.citybike

import com.itechart.citybike.parser.BikeTrip
import org.apache.logging.log4j.scala.Logging

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object BikeStats extends Logging {

  def run(data: List[List[BikeTrip]]): Future[Map[Int, (Int, Int)]] = {
    val countBikeStatsFutureSeq: List[Future[Seq[(Int, (Int, Int))]]] = data.map(countBikeStats)
    val result: Future[Map[Int, (Int, Int)]] = Future.sequence(countBikeStatsFutureSeq).map(result => {
      result.flatten.groupBy(_._1).mapValues(x => (x.map(_._2._1).sum, x.map(_._2._2).sum))
    })
    result
  }

  def countBikeStats(data: List[BikeTrip]): Future[Seq[(Int, (Int, Int))]] = Future {
    data.groupBy(_.bikeId).mapValues(x => (x.size, x.map(_.tripDuration).sum)).toSeq.sortBy(_._2._2)(Ordering[Int].reverse)
  }
}
