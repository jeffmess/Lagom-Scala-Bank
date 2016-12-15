package controllers

import javax.inject._

import auth.Authentification
import auth.Authentification.{AuthStatus, Authentified, NoAuthentified, User}
import org.lead.auth.jwt.AuthJwt
import play.api.libs.json._
import play.api.mvc._


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller with AuthJwt {

  case class UserAuth(login: String, pwd: String)

  def index = Action {

    Ok(views.html.index())
  }

  def account(id: String) = Action { (req: Request[AnyContent]) =>
    Ok(views.html.account(id))
  }


  def verif = Action { implicit request =>
    val authHeader = request.headers.get(Authentification.headerKey)

    extractClaimsValue(authHeader.getOrElse("noTokenFound"), "login") match {
      case Left(ex) => Forbidden(ex.getMessage)
      case Right(login) => Ok(JsObject(Seq(("login", JsString(login)))))
    }


  }

  def auth = Action(parse.json) {
    request => {

      import play.api.libs.json._
      import play.api.libs.json.Reads._
      import play.api.libs.functional.syntax._

      implicit val userAuthReader: Reads[UserAuth] = (
        (JsPath \ "login").read[String] and
          (JsPath \ "pwd").read[String]
        ) (UserAuth.apply _)

      val user = request.body.as[UserAuth]
      checkAuth(user) match {
        case Authentified(u) => Ok(Json.toJson(Map("jwtToken" -> generateToken(Map("login" -> u.name)))))
        case NoAuthentified => Forbidden("User not authorized")
      }


    }
  }

  private def checkAuth(u: UserAuth): AuthStatus = Authentified(User(u.login, "UUID"))


}
