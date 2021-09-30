name := "DeployTest"

version := "0.1"

scalaVersion := "2.13.6"

//Cats_Library
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9"

//Project_Util
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.12",
  "log4j" % "log4j" % "1.2.17",
  "org.projectlombok" % "lombok" % "1.18.20" % "provided",
  "org.json" % "json" % "20210307"
)

//O/R_Mapper
libraryDependencies ++= Seq(
  "org.hibernate" % "hibernate-core" % "5.3.7.Final",
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "javax.activation" % "activation" % "1.1.1",
  "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.1"
)
