package ch.fhnw.ima.bimgur
package service

import cats.data.Xor
import org.scalatest.FlatSpec

class FormServiceSpec extends FlatSpec {

  it should "decode FormData from Activiti JSON" in {
    val json =
      """{
          "formKey": null,
          "deploymentId": "20",
          "processDefinitionId": "bimgur-master:1:25",
          "processDefinitionUrl": "http://0.0.0.0:8080/activiti-rest/service/repository/process-definitions/bimgur-master:1:25",
          "taskId": null,
          "taskUrl": null,
          "formProperties": [
            {
              "id": "analysis-name",
              "name": "Analysis Name",
              "type": "string",
              "value": null,
              "readable": true,
              "writable": true,
              "required": true,
              "datePattern": null,
              "enumValues": []
            },
            {
              "id": "workflow-key",
              "name": "Analysis Workflow Key",
              "type": "string",
              "value": "bimgur-demo-japanese-numbers",
              "readable": true,
              "writable": true,
              "required": true,
              "datePattern": null,
              "enumValues": []
            },
            {
              "id": "image-path",
              "name": "Image Path",
              "type": "string",
              "value": "japan",
              "readable": true,
              "writable": true,
              "required": true,
              "datePattern": null,
              "enumValues": []
            }
          ]
        }"""
    FormService.decodeFormData(json) match {
      case Xor.Right(formData) =>
        assert(formData.formProperties.length == 3)
      case _ => fail("Decoding JSON failed")
    }
  }

}