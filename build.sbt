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

excludedFiles in assembly := { (bases: Seq[File]) =>
  bases flatMap { base =>
    (base / "META-INF" * "*").get collect {
      case f if f.getName == "NOTICE.txt" => f
      case f if f.getName == "NOTICE" => f
      case f if f.getName == "MANIFEST.MF" => f
      case f if f.getName == "LICENSE.txt" => f
      case f if f.getName == "LICENSE" => f
    }
  }}

