name := """play-java-seed"""
organization := "Books-Service"
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayJava)
scalaVersion := "2.11.7"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.12.14"
libraryDependencies ++= Seq(
//  "org.mongodb" % "mongo-java-driver" % "3.12.0",
  "org.projectlombok" % "lombok" % "1.18.2",
  "dev.morphia.morphia" % "core" % "1.5.8",
  "org.easytesting" % "fest-assert" % "1.4" % "test",
  javaWs
)
libraryDependencies += "com.typesafe.play" % "play-ws_2.11" % play.core.PlayVersion.current
libraryDependencies += "com.typesafe.play.modules" %% "play-modules-redis" % "2.4.1"
// enable Play cache API (based on your Play version)
// https://mvnrepository.com/artifact/redis.clients/jedis
libraryDependencies += "redis.clients" % "jedis" % "3.7.0"
libraryDependencies += "com.typesafe.play" %% "filters-helpers" % "2.5.8"

libraryDependencies += "junit" % "junit" % "4.13"
libraryDependencies += "org.mockito" % "mockito-inline" % "4.6.1" % "test"

