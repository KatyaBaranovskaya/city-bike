name := "city-bike"

version in ThisBuild := "0.1.0-SNAPSHOT"

organization in ThisBuild := "com.itechart-group"

scalaVersion in ThisBuild := "2.12.3"



lazy val root = (project in file("."))
  .settings(
    name := "Main",
    libraryDependencies += "org.apache.logging.log4j" % "log4j-api-scala_2.12" % "11.0"
  )
