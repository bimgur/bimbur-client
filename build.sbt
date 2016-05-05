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
  "be.doeraene" %%% "scalajs-jquery" % "0.9.0",
  "org.singlespaced" %%% "scalajs-d3" % "0.3.3",
  "com.thoughtworks.binding" %%% "dom" % "latest.release"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",
  "org.webjars" % "bootstrap" % "3.3.6" / "bootstrap.js" minified "bootstrap.min.js" dependsOn "2.1.4/jquery.js"
)

persistLauncher in Compile := true

persistLauncher in Test := false

mainClass in Compile := Some("ch.eawag.bimgur.App")