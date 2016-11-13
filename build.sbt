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
      lagomJavadslPersistence,
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



lazy val helloworldApi = project("helloworld-api", "1.0-SNAPSHOT")
  .settings(
    libraryDependencies += lagomJavadslApi
  )

lazy val helloworldImpl = project("helloworld-impl", "1.0-SNAPSHOT")
  .enablePlugins(LagomJava)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(helloworldApi)

lazy val hellostreamApi = project("hellostream-api", "1.0-SNAPSHOT")
  .settings(
    libraryDependencies += lagomJavadslApi
  )

lazy val hellostreamImpl = project("hellostream-impl", "1.0-SNAPSHOT")
  .enablePlugins(LagomJava)
  .dependsOn(hellostreamApi, helloworldApi)
  .settings(
    libraryDependencies += lagomJavadslTestKit
  )
