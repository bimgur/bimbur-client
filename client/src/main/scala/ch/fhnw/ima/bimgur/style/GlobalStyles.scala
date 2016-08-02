package ch.fhnw.ima.bimgur.style

import scala.language.postfixOps
import scalacss.Defaults._

/**
  * Defines CSS styles that can be referenced from Scala.js React tags.
  * The actual CSS will be inlined into the index.html document.
  * Motivation: If we ever switch from Bootstrap to e.g. Materialize, this should be
  * the only place that requires CSS changes...
  */
object GlobalStyles extends StyleSheet.Inline {

  import dsl._

  def styleWrap(classNames: String*) = style(addClassNames(classNames: _*))

  // a bootstrap container with customizations
  val container = style(
    addClassNames("container"),
    paddingTop(20 px)
  )

  val defaultButton = styleWrap("btn btn-default")

  val navTabs = styleWrap("nav", "nav-tabs")

  val active = styleWrap("active")
  val nonActive = styleWrap()

  // bootstrap's info-style alert box
  val infoBox = styleWrap("alert", "alert-info")

  val table = styleWrap("table")

}
