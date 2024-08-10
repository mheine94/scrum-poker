package model

import play.api.libs.json.{Format, Json}

case class Vote(player: String, choice: String){

}
object Vote {
  implicit val format: Format[Vote] = Json.format[Vote]
}