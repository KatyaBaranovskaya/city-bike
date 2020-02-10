package com.itechart.citybike.writer

import java.io.{File, PrintWriter}

class CsvWriter {

  def writeToFile(fileName: String, values: Seq[String]): Unit = {
    val pw = new PrintWriter(new File(fileName))
    values.foreach(pw.println)

    pw.close()
  }
}
