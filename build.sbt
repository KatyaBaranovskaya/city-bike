name := "city-bike"

version in ThisBuild := "0.1.0-SNAPSHOT"

organization in ThisBuild := "com.itechart-group"

scalaVersion in ThisBuild := "2.12.3"



lazy val root = (project in file("."))
  .settings(
    name := "Main",
    libraryDependencies ++= Seq(
      "org.apache.logging.log4j" % "log4j-api-scala_2.12" % "11.0",
      "org.apache.logging.log4j" % "log4j-api" % "2.13.0",
      "org.apache.logging.log4j" % "log4j-core" % "2.13.0",
      "com.typesafe" % "config" % "1.4.0",
      "org.scalatest" %% "scalatest" % "3.0.8" % "test"
    )
  )
