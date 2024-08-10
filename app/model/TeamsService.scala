package model

import javax.inject.Singleton


@Singleton
class TeamsService {
  private var teams : List[Team] = List()

  def findByName(teamName: String): Option[Team] = {
    val searchResult = teams.find(team => team.teamName.equals(teamName))
    searchResult
  }

  def createTeam(teamName: String): Option[Team] = {
    val teamNameIsTaken = teams.exists(team => team.teamName.equals(teamName))
    if(teamNameIsTaken){
      return Option.empty
    }

    val team = new Team(teamName);
    teams = teams :+ team

    Option(team)
  }

  def joinTeam(teamName: String, playerName: String): Option[Team] = {
    val searchResult = teams.find(team => team.teamName.equals(teamName))
    if(searchResult.isEmpty){
      return Option.empty
    }
    val team = searchResult.get;
    team.players = team.players :+ playerName
    Option(team)
  }
}
