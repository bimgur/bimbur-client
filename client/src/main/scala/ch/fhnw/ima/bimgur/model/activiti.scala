package ch.fhnw.ima.bimgur
package model

import cats.data.Xor
import io.circe.Decoder

object activiti {

  // Type-Safe Activiti Ids

  type ProcessDefinitionId = String
  type ProcessInstanceId = String

  // Translate between Activiti and Bimgur Lingo

  type Analysis = ProcessInstance

  // Activiti REST DTOs

  final case class ProcessInstanceList(data: Seq[ProcessInstance])

  final case class ProcessInstance(id: ProcessInstanceId, processDefinitionId: ProcessDefinitionId, variables: Seq[Variable])

  final case class FormData(processDefinitionId: ProcessDefinitionId, formProperties: Seq[FormProperty])

  final case class FormProperty(name: String)

  final case class Variable(name: String, `type`: String, value: VariableValue)

  object Variable {

    // Manual JSON decoding of variables to handle type-safe values.
    // Not all types supported yet! http://www.activiti.org/userguide/#restVariables
    // Example of Activiti's JSON:
    //
    //  "variables": [
    //    {
    //      "name": "one",
    //      "type": "string",
    //      "value": "kermit"
    //    },
    //    {
    //      "name": "two",
    //      "type": "long",
    //      "value": 42
    //    },
    //    {
    //      "name": "three",
    //      "type": "boolean",
    //      "value": true
    //    }
    //  ]
    //
    //
    implicit val decodeVariable: Decoder[Variable] = Decoder.instance(c => {

      val `type` = c.downField("type").as[String]

      val variableValueField = c.downField("value")

      val variableValue = `type`.flatMap {
        case "string" => variableValueField.as[String].map(StringVariableValue)
        case "long" => variableValueField.as[Long].map(LongVariableValue)
        case "double" => variableValueField.as[Double].map(DoubleVariableValue)
        case "boolean" => variableValueField.as[Boolean].map(BooleanVariableValue)
        case missingType @ _ => Xor.right(UnsupportedVariableValue(missingType))
      }

      for {
        name <- c.downField("name").as[String]
        t <- `type`
        vv <- variableValue
      } yield Variable(name, t, vv)

    }
    )

  }

  sealed trait VariableValue {
    type Value
    def value: Value
  }

  final case class StringVariableValue(value: String) extends VariableValue {
    type Value = String
  }

  final case class LongVariableValue(value: Long) extends VariableValue {
    type Value = Long
  }

  final case class DoubleVariableValue(value: Double) extends VariableValue {
    type Value = Double
  }

  final case class BooleanVariableValue(value: Boolean) extends VariableValue {
    type Value = Boolean
  }

  final case class UnsupportedVariableValue(missingType: String) extends VariableValue {
    type Value = String
    override def value = s"(type '$missingType' not yet supported)"
  }

}
