import sbt._

organization in ThisBuild := "com.guizmaii"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lagomCassandraCleanOnStart in ThisBuild := true

val jacksonVersion = "2.8.3"

// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(scalacOptions in compile += "-parameters")

def project(id: String, versionV: String) = Project(id = id, base = file(id))
  .settings(version := versionV)
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))

def scalaProject(id: String, versionV: String) = project(id, versionV)
  .settings(jacksonParameterNamesJavacSettings: _*)
  .settings(
    scalacOptions in Compile += "-Xexperimental", // this enables Scala lambdas to be passed as Java SAMs
    // Come from here:
    //  - https://tpolecat.github.io/2014/04/11/scalac-flags.html
    //  - http://pedrorijo.com/blog/scala-compiler-review-code-warnings/
    scalacOptions ++= Seq(
      "-deprecation",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-unchecked",
      //      "-Xfatal-warnings",
      "-Xlint",
      "-Xlint:missing-interpolator",
      "-Yno-adapted-args",
      "-Ywarn-unused",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture",
      "-Ywarn-unused-import"
    )
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion, // actually, only api projects need this
      "org.scalacheck" %% "scalacheck" % "1.13.2" % "test",
      "org.scalactic" %% "scalactic" % "3.0.0" % "test",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.mockito" % "mockito-all" % "1.10.19" % "test"
    )
  )

def scalaServiceApi(id: String, versionV: String) = scalaProject(id, versionV)
  .settings(libraryDependencies += lagomJavadslApi)
  .dependsOn(utils)

def scalaServiceImpl(id: String, versionV: String) = scalaProject(id, versionV)
  .enablePlugins(LagomJava)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(utils)

lazy val utilsVersion = "1.0-SNAPSHOT"
lazy val utils = scalaProject("utils", utilsVersion)
  .settings(
    version := utilsVersion,
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslServer,
      lagomJavadslPersistence,
      "com.markatta" %% "futiles" % "1.1.3",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion
    )
  )

// ----------------------------------------------
// Services
// ----------------------------------------------

lazy val frontEndName = "front-end"
lazy val frontEnvVersion = "1.0-SNAPSHOT"
lazy val frontEnd: Project = project(frontEndName, frontEnvVersion)
  .enablePlugins(PlayScala, LagomPlay)
  .settings(routesGenerator := InjectedRoutesGenerator)
  .dependsOn(authJwtlib)

lazy val bankAccountVersion = "1.0-SNAPSHOT"
lazy val bankAccountApi = scalaServiceApi("bank-account-api", bankAccountVersion)
lazy val bankAccountImpl = scalaServiceImpl("bank-account-impl", "1.0-SNAPSHOT")
  .dependsOn(bankAccountApi)


lazy val authJwtlib = (Project("authJwtlib",file("authJwtlib"))).settings(
  version := "1.0-SNAPSHOT",
  libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.1",
    "com.pauldijou" %% "jwt-core" % "0.9.2",
    "com.typesafe.play" % "play-json_2.11" % "2.5.10"))