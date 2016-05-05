lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    name := "Bimgur Client",
    scalaVersion := "2.11.8",
    version := "0.1-SNAPSHOT",
    scalacOptions += "-feature"
  )

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "upickle" % "0.4.0",
  "com.lihaoyi" %%% "scalatags" % "0.5.5",
  "com.lihaoyi" %%% "scalarx" % "0.2.8",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.0"
)

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",
  "org.webjars" % "bootstrap" % "3.3.6" / "bootstrap.js" minified "bootstrap.min.js" dependsOn "2.1.4/jquery.js"
)

persistLauncher in Compile := true

persistLauncher in Test := false

mainClass in Compile := Some("ch.eawag.bimgur.App")