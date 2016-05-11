package ch.eawag.bimgur.controller

import ch.eawag.bimgur.model.{BimgurModel, Group, User}
import ch.eawag.bimgur.service.{GroupService, UserService}
import diode._
import diode.data._
import diode.react.ReactConnector

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object BimgurController {

  val activitiRestUrl = "http://kermit:kermit@localhost:8090/activiti-rest/service"
  val userService = UserService(activitiRestUrl)
  val groupService = GroupService(activitiRestUrl)

  // Actions

  case class UpdateUsers(potResult: Pot[Seq[User]] = Empty) extends PotAction[Seq[User], UpdateUsers] {
    override def next(newPotResult: Pot[Seq[User]]) = UpdateUsers(newPotResult)
  }

  case class UpdateGroups(potResult: Pot[Seq[Group]] = Empty) extends PotAction[Seq[Group], UpdateGroups] {
    override def next(newPotResult: Pot[Seq[Group]]): UpdateGroups = UpdateGroups(newPotResult)
  }

  // Action Handlers

  class UserHandler(modelRW: ModelRW[BimgurModel, Pot[Seq[User]]]) extends ActionHandler(modelRW) {

    override def handle = {
      case action: UpdateUsers =>
        val updateF = action.effect(userService.getUsers)(identity)
        action.handleWith(this, updateF)(PotAction.handler())
    }

  }

  class GroupHandler(modelRW: ModelRW[BimgurModel, Pot[Seq[Group]]]) extends ActionHandler(modelRW) {

    override def handle = {
      case action: UpdateGroups =>
        val updateF = action.effect(groupService.getRandomGroups)(identity)
        action.handleWith(this, updateF)(PotAction.handler())
    }

  }

  // Circuit

  object BimgurCircuit extends Circuit[BimgurModel] with ReactConnector[BimgurModel] {

    override protected def initialModel = BimgurModel(Empty, Empty)

    override protected val actionHandler = composeHandlers(
      new UserHandler(zoomRW(_.users)((m, v) => m.copy(users = v))),
      new GroupHandler(zoomRW(_.groups)((m, v) => m.copy(groups = v)))
    )

    override def handleError(msg: String): Unit = {
      val name = BimgurController.getClass.getSimpleName
      println(s"[$name] Error: $msg")
    }

  }

}
