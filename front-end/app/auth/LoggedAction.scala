package auth

import org.lead.auth.jwt.AuthJwt
import play.api.mvc.{ActionBuilder, Request, Result}
import play.api.mvc.Results._

import scala.concurrent.Future

/**
  * Created by fsznajderman on 10/12/2016.
  */
class LoggedAction extends ActionBuilder[Request] with AuthJwt{



  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {

    request.headers.get(Authentification.headerKey) match {
      case Some(token) => {
        if(validateToken(token)) {
          block(request)
        }else{
          Future.successful(Forbidden)
        }
      }
      case None => Future.successful(Forbidden)
    }

  }

}
