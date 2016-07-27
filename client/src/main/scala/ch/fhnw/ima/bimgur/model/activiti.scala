package ch.fhnw.ima.bimgur.model

object activiti {

  // http://hseeberger.github.io/blog/2013/10/25/attention-seq-is-not-immutable

  type Seq[+A] = scala.collection.Seq[A]
  val Seq = scala.collection.Seq

  // Type-Safe Activiti Ids

  type ProcessDefinitionId = String
  type ProcessInstanceId = String

  // Translate between Activiti and Bimgur Lingo

  type Analysis = ProcessInstance

  // Activiti REST DTOs

  final case class ProcessInstanceList(data: Seq[ProcessInstance])

  final case class ProcessInstance(id: ProcessInstanceId, processDefinitionId: ProcessDefinitionId)

  final case class FormData(processDefinitionId: ProcessDefinitionId, formProperties: Seq[FormProperty])

  final case class FormProperty(name: String)

}
