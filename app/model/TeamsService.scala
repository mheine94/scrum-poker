package model

import model.PlayerStates.THINKING
import model.PlayerStates.DECIDED

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
    team.players = team.players :+ Player(playerName, null, THINKING);
    Option(team)
  }

  def removeFromTeam(teamName: String, playerName: String): Option[Team] = {
    val searchResult = teams.find(team => team.teamName.equals(teamName))
    if(searchResult.isEmpty){
      return Option.empty
    }
    val team = searchResult.get;
    team.players = team.players.filterNot(player => player.name == playerName)
    Option(team)
  }

  def vote(teamName: String, vote: Vote) = {
     val teamOpt = findByName(teamName)
    teamOpt match {
      case Some(team) => {
        val playerOpt = team.players.find(p => p.name .equals(vote.player))
        playerOpt match {
          case Some(player) => {
            val index = team.players.indexOf(player)
            team.players = team.players.updated(index, Player(player.name, vote.choice, DECIDED))
          }
          case None => println(s"Error: player \"${vote.player}\" is not on the team \"$teamName\"")
        }
      }
      case None => println(s"Error: vote for non existing team \"$teamName\"")
    }
  }

  def everyOneVoted(teamName: String) = {
    val teamOpt = findByName(teamName)
    teamOpt match {
      case Some(team) => {
        team.players.forall(p => p.state == DECIDED)
      }
      case None => {
        println(s"Error: team \"$teamName\" does not exist")
        false
      }
    }
  }

  def startNewRound(teamName: String): Unit = {
    val teamOpt = findByName(teamName)
    teamOpt match {
      case Some(team) => team.players.foreach(p => {
        p.state = PlayerStates.THINKING
        p.choice = null;
      })
      case None => println(s"Error: team \"$teamName\" does not exist")
    }
  }

  def isTeamMember(teamName: String, playerName: String)= {
    val teamOpt = findByName(teamName)
    teamOpt match {
      case Some(team) => {
        team.players.exists(p => p.name .equals(playerName))
      }
      case None => {
        println(s"Error: team \"$teamName\" does not exist")
        false
      }
    }
  }
}
