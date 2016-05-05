package ch.eawag.bimgur.view

import ch.eawag.bimgur.view.page.Page.PageId
import rx.core.Var

class PresentationModel {

  val activePage: Var[Option[PageId]] = Var(None)

}
