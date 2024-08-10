package model

class Team(var teamName : String , var players: List[String]) {

  def this(teamName: String) {
    this(teamName, List())
  }
}
