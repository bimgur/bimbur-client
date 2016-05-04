package ch.eawag.bimgur

import ch.eawag.bimgur.page.{GroupPage, UserPage}

import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import scalatags.JsDom.all._

object App extends JSApp {

  override def main() = {

    val contents = div(
      h1("Users"),
      new UserPage().render,
      h1("Groups"),
      new GroupPage().render
    )

    document.body.appendChild(contents.render)
  }

}
