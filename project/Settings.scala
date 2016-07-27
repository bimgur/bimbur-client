import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Settings {

  val activitiCustomName = "bimgur-activiti-custom"
  val clientName = "bimgur"

  val version = "0.1.0-SNAPSHOT"

  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  object versions {
    val scala = "2.11.8"

    // activiti custom
    val activiti = "5.21.0"
    val servlet = "3.1.0"
    val slf4j = "1.7.21"

    // client Scala.JS libraries
    val scalaJsReact = "0.11.1"
    val diode = "1.0.0"
    val upickle = "0.4.1"

    // client JS dependencies
    val react = "15.2.1"
    val bootstrap = "3.3.2"
    val jQuery = "1.11.1"
  }

  val activitiCustomDependencies = Def.setting(Seq(
    "org.activiti" % "activiti-engine" % versions.activiti,
    "javax.servlet" % "javax.servlet-api" %  versions.servlet % Provided,
    "org.slf4j" % "slf4j-simple" % versions.slf4j
  ))

  val clientDependencies = Def.setting(Seq(
    "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalaJsReact,
    "com.lihaoyi" %%% "upickle" % versions.upickle,
    "me.chrons" %%% "diode" % versions.diode,
    "me.chrons" %%% "diode-react" % versions.diode
  ))

  val clientJsDependencies = Def.setting(Seq(
    "org.webjars.bower" % "react" % versions.react / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % versions.react / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
    "org.webjars" % "jquery" % versions.jQuery / "jquery.js" minified "jquery.min.js",
    "org.webjars" % "bootstrap" % versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js",
    RuntimeDOM % "test"
  ))

}