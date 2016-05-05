package ch.eawag.bimgur.view

import ch.eawag.bimgur.view.page.Page.PageId
import ch.eawag.bimgur.view.page.UserPage
import com.thoughtworks.binding.Binding.{Var, Vars}

class PresentationModel {

  val activePage = Var[Option[PageId]](Some(UserPage.pageId))

}
