package controllers

import model.{CreateTeamDto, TeamsService}

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.i18n.MessagesApi
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val messageApi: MessagesApi,  val teamsService: TeamsService) extends BaseController {
  val createTeamForm = Form(
    mapping(
      "team-name" -> text
    )(CreateTeamDto.apply)(CreateTeamDto.unapply)
  )
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(createTeamForm)(messageApi.preferred(request)))
  }

  def create(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val createTeamDto = createTeamForm.bindFromRequest().get

    val result = teamsService.createTeam(createTeamDto.teamName)

    if(result.isEmpty){
      Conflict(s"team ${createTeamDto.teamName} already exists")
    }

    Redirect(s"/team/${createTeamDto.teamName}", MOVED_PERMANENTLY)
  }
}
