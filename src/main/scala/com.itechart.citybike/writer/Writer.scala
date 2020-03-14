package com.itechart.citybike.writer

import java.io.{File, PrintWriter}

class Writer {

  def writeToFile(fileName: String, values: Seq[Any]): Unit = {
    val pw = new PrintWriter(new File(fileName))
    values.foreach(pw.println)

    pw.close()
  }
}
