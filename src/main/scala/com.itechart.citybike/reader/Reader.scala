package com.itechart.citybike.reader

import java.io.{File, FileNotFoundException, IOException}

import org.apache.logging.log4j.scala.Logging

import scala.io.Source

class Reader extends Logging {

  def readFile(fileName: String): List[String] = {
    try {
      val source = Source.fromResource(fileName)
      val result = source.getLines().toList

      source.close()
      result
    }
    catch {
      case e: FileNotFoundException => {
        logger.error("File not found exception")
        throw new RuntimeException("File not found exception", e)
      }
      case e: IOException => {
        logger.error("IO Exception")
        throw new RuntimeException("IO Exception", e)
      }
    }
  }

  def getListOfFiles(dir: String): List[String] = {
    val path = getClass.getResource(dir)
    if (path != null) {
      val folder = new File(path.getPath)
      if (folder.exists && folder.isDirectory) {
        return folder.listFiles.filter(_.isFile).map(_.getName).toList
      }
    }
    List.empty
  }
}

