lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    name := "Bimgur Client",
    scalaVersion := "2.11.8",
    version := "0.1-SNAPSHOT"
  )

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "upickle" % "0.4.0",
  "com.lihaoyi" %%% "scalatags" % "0.5.5"
)

persistLauncher in Compile := true

persistLauncher in Test := false

mainClass in Compile := Some("ch.eawag.bimgur.App")