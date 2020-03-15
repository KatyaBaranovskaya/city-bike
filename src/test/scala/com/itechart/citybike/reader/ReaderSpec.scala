package com.itechart.citybike.reader

import org.scalatest.{FlatSpec, Matchers}

class ReaderSpec extends FlatSpec with Matchers {

  it should "read file and return data" in {
    val reader = new Reader()
    val data = reader.readFile("test.csv")

    assert(data.size == 7)
  }

  it should "trow exception when file not found " in {
    val reader = new Reader()
    a [RuntimeException] should be thrownBy {
      reader.readFile("bike.csv")
    }
  }
}
