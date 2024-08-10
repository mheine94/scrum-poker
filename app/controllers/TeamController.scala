package controllers

import model.{CreateTeamDto, JoinTeamDto, Team, TeamsService, Vote}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Cookie, DiscardingCookie, Request}

import javax.inject.{Inject, Singleton}

@Singleton
class TeamController @Inject()(val controllerComponents: ControllerComponents, val messageApi: MessagesApi, val teamsService: TeamsService) extends BaseController {
  val PLAYER_NAME_COOKIE_NAME = "playerName";

  val JOIN_TEAM_FORM = Form(
    mapping(
      "player-name" -> text
    )(JoinTeamDto.apply)(JoinTeamDto.unapply)
  )

  def poker(teamName: String) = Action { implicit request =>

    val searchResult = teamsService.findByName(teamName)
    if (searchResult.isEmpty) {
      Redirect(controllers.routes.HomeController.index(), MOVED_PERMANENTLY)
    }else{
      val team = searchResult.get

      val playerNameOpt: Option[String] = request.cookies.get(PLAYER_NAME_COOKIE_NAME).map(_.value)

      playerNameOpt match {
        case Some(playerName) =>{
          if(team.players.contains(playerName)){
            Ok(views.html.poker(team.teamName, playerName, team.players, List("0.25", "0.5", "1", "2")))
          }else{
            Ok(views.html.choosename(teamName, JOIN_TEAM_FORM)(messageApi.preferred(request))).discardingCookies(DiscardingCookie(PLAYER_NAME_COOKIE_NAME))
          }
        }
        case None =>
          Ok(views.html.choosename(teamName, JOIN_TEAM_FORM)(messageApi.preferred(request))).discardingCookies(DiscardingCookie(PLAYER_NAME_COOKIE_NAME))
      }
    }
  }


  def vote(teamName: String): Action[JsValue] = Action(parse.json) { implicit request  =>
    // TODO
    request.body.validate[Vote].fold(
      errors => BadRequest(Json.obj("status" -> "error", "message" -> JsError.toJson(errors))),
      vote => {
        println(s"team $teamName: vote cast by ${vote.player} for choice ${vote.choice}");
        Ok
      }
    )
  }

  def joinTeam(teamName: String): Action[AnyContent] = Action { implicit request =>
    val joinTeamDto = JOIN_TEAM_FORM.bindFromRequest().get
    val result = teamsService.joinTeam(teamName, joinTeamDto.playerName)

    result match {
      case Some(team: Team) =>{
        val playerNameCookie = Cookie(PLAYER_NAME_COOKIE_NAME, joinTeamDto.playerName)
        Redirect(controllers.routes.TeamController.poker(team.teamName)).withCookies(playerNameCookie)
      }
      case None => Redirect(controllers.routes.HomeController.index(), MOVED_PERMANENTLY)
    }
  }

}
