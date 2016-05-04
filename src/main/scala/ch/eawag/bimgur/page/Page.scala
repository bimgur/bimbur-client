package ch.eawag.bimgur.page

import ch.eawag.bimgur.page.Page.PageId
import org.scalajs.dom

trait Page {

  val pageId: PageId

  def render: dom.Element

}

object Page {
  type PageId = String
}
