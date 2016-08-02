package ch.fhnw.ima.bimgur
package model

import ch.fhnw.ima.bimgur.model.activiti.{Analysis, FormData}
import diode.data.Pot

case class BimgurModel(masterFormData: Pot[FormData], analyses: Pot[Seq[Analysis]])