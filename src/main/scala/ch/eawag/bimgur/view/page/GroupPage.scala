package ch.eawag.bimgur.view.page

import ch.eawag.bimgur.model.{Group, GroupList}
import com.thoughtworks.binding.Binding.Vars
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html._
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object GroupPage extends Page {

  val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/groups"

  val pageId = "groups"

  val groups = Vars.empty[Group]

  val requestFuture = Ajax.get(Url)
  requestFuture.onSuccess { case response =>
    groups.get ++= read[GroupList](response.responseText).data
  }
  requestFuture.onFailure { case error =>
    println(error)
  }

  @dom
  def groupList: Binding[UList] = {
    <ul>
      {for (group <- groups) yield {
      <li>
        {group.name}
      </li>
    }}
    </ul>
  }

}
