lazy val root = (project in file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    name := "Bimgur Client",
    scalaVersion := "2.11.8",
    version := "0.1-SNAPSHOT",
    scalacOptions += "-feature"
  )

val scalaJsReactVersion = "0.11.1"

libraryDependencies ++= Seq(
  "com.github.japgolly.scalajs-react" %%% "extra"        % scalaJsReactVersion,
  "com.lihaoyi"                       %%% "upickle"      % "0.4.0",
  "org.singlespaced"                  %%% "scalajs-d3"   % "0.3.3"
)

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % "15.0.1" / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
  "org.webjars.bower" % "react" % "15.0.1" / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM"
)

persistLauncher in Compile := true

persistLauncher in Test := false

mainClass in Compile := Some("ch.eawag.bimgur.App")

relativeSourceMaps := true