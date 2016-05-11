package ch.eawag.bimgur.model

import diode.data.Pot

case class BimgurModel(users: Pot[Seq[User]], groups: Pot[Seq[Group]])