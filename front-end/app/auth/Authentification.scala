package auth

/**
  * Created by fsznajderman on 09/12/2016.
  */
object Authentification {

  val headerKey = "Authorization"

  case class User(name:String, id:String)

  sealed trait AuthStatus

  case class Authentified(user : User) extends AuthStatus
  case object NoAuthentified extends AuthStatus

}
