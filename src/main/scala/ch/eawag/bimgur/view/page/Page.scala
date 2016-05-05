package ch.eawag.bimgur.view.page

import ch.eawag.bimgur.view.page.Page.PageId

trait Page {
  val pageId: PageId
}

object Page {
  type PageId = String
}
