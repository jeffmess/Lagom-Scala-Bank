package com.guizmaii

import java.util
import java.util.Optional

import akka.util.ByteString
import com.lightbend.lagom.javadsl.api.deser.{DeserializationException, ExceptionMessage, ExceptionSerializer, RawExceptionMessage}
import com.lightbend.lagom.javadsl.api.transport.{MessageProtocol, TransportErrorCode}

/**
  * Created by fsznajderman on 20/11/2016.
  */

class AuthentificationException extends ExceptionSerializer {

  val mp = MessageProtocol.fromContentTypeHeader(Optional.of("application/json"))

  override def serialize(exception: Throwable, accept: util.Collection[MessageProtocol]): RawExceptionMessage = {

    exception match {
      case e: ForbiddenException => new RawExceptionMessage(TransportErrorCode.Forbidden, mp, ByteString(e.getMessage.getBytes))
      case _ =>
        new RawExceptionMessage(TransportErrorCode.InternalServerError, mp, ByteString("Unknown error has been throwed"))
    }

  }

  override def deserialize(message: RawExceptionMessage): Throwable =
    new DeserializationException(message.errorCode(), new ExceptionMessage(message.messageAsText(), message.messageAsText()))
}