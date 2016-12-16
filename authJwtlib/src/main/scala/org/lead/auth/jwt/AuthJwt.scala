package org.lead.auth.jwt

import pdi.jwt.{Jwt, JwtAlgorithm, JwtUtils}
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}

/**
  * Created by fsznajderman on 19/11/2016.
  */


trait AuthJwt {

  def generateToken(entries: Map[String, String]): String = {


    val claims = JwtUtils.hashToJson(entries.toSeq)
    Jwt.encode(claims, Settings.secretkey, JwtAlgorithm.HS256)

  }

  def validateToken(token: String): Boolean = {
    Try {
      Jwt.validate(token, Settings.secretkey, Seq(JwtAlgorithm.HS256))
    }.isSuccess
  }


  def extractClaimsValue(token: String, claimsKey: String): Either[Throwable, String] = {
    Jwt.decode(token, Settings.secretkey, Seq(JwtAlgorithm.HS256)) match {
      case Success(r) => Right((Json.parse(r) \ claimsKey).get.toString())
      case Failure(ex) => Left(ex)
    }
  }


}

