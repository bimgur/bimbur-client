import Settings.versions

lazy val root = (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings(
    name := Settings.name,
    scalaVersion := Settings.versions.scala,
    version := Settings.version,
    scalacOptions += "-feature"
  )


libraryDependencies ++= Seq(
  "com.github.japgolly.scalajs-react" %%% "extra"        % versions.scalaJsReact,
  "com.lihaoyi"                       %%% "upickle"      % "0.4.0",
  "org.singlespaced"                  %%% "scalajs-d3"   % "0.3.3",
  "me.chrons"                         %%% "diode"        % versions.diode,
  "me.chrons"                         %%% "diode-react"  % versions.diode
)

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % versions.react / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
  "org.webjars.bower" % "react" % versions.react / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
  "org.webjars" % "jquery" % versions.jQuery / "jquery.js" minified "jquery.min.js",
  "org.webjars" % "bootstrap" % versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js"
)

persistLauncher in Compile := true

persistLauncher in Test := false

mainClass in Compile := Some("ch.eawag.bimgur.App")

relativeSourceMaps := true