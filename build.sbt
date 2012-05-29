import AssemblyKeys._

organization := "com.gu"

name := "ec2-host-provider"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.3.4",
  "net.liftweb" %% "lift-json" % "2.4"
)

assemblySettings

jarName in assembly <<= name (_ + ".jar")



