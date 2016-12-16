package controllers

import javax.inject._

import auth.{Authentification, LoggedAction}
import auth.Authentification.{AuthStatus, Authentified, NoAuthentified, User}
import ch.qos.logback.classic.joran.action.LoggerAction
import org.lead.auth.jwt.AuthJwt
import play.api.libs.json._
import play.api.mvc._


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {


  def index = Action {

    Ok(views.html.index())
  }

  def account(id: String) = LoggedAction { (req: Request[AnyContent]) =>
    Ok(views.html.account(id))
  }


}
