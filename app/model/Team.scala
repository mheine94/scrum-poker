package model

class Team(var teamName : String , var players: List[Player]) {
  def this(teamName: String) {
    this(teamName, List())
  }
}
