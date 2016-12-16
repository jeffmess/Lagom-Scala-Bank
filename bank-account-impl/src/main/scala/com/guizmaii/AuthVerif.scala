package com.guizmaii

/**
  * Created by fsznajderman on 15/12/2016.
  */
import com.lightbend.lagom.javadsl.server.{HeaderServiceCall, ServerServiceCall}
import org.lead.auth.jwt.AuthJwt

/**
  * Created by fsznajderman on 19/11/2016.
  */
trait AuthVerif extends AuthJwt {

  /**
    * method aim to validate JWT tokens
    */

  def authentification[Request, Response](block: ServerServiceCall[Request, Response]): ServerServiceCall[Request, Response] = {

    HeaderServiceCall.compose(requestHeader => {
      if (!validateToken(requestHeader.getHeader("Authorization").orElse("no_token"))) {
        throw new ForbiddenException("token invalid")
      }
      block
    })

  }

}