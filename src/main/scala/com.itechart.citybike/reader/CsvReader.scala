package com.itechart.citybike.reader

import com.itechart.citybike.parser.BikeTrip
import com.itechart.citybike.parser.CsvParser.parseLine

import scala.io.Source

class CsvReader {

  def readFile(fileName: String): Seq[BikeTrip] = {
    val source = Source.fromResource(fileName)
    val lineIterator = source.getLines()

    val result = lineIterator.drop(1).map(parseLine).toList

    source.close()
    result
  }
}
