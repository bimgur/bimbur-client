package ch.eawag.bimgur.view.page

import ch.eawag.bimgur.view.page.Page.PageId
import org.scalajs.dom

trait Page {

  val pageId: PageId

  def content: dom.Element

}

object Page {
  type PageId = String
}
