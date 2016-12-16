package converter

import java.util.concurrent.CompletionStage

import akka.japi.Pair
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.{MessageProtocol, RequestHeader, ResponseHeader}
import com.lightbend.lagom.javadsl.server.{HeaderServiceCall, ServerServiceCall}
import org.pcollections.HashTreePMap

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

object ServiceCallConverter extends CompletionStageConverters {

  implicit def liftToServiceCall[Request, Response](f: Request => Future[Response]): ServiceCall[Request, Response] =
    new ServiceCall[Request, Response] {
      def invoke(request: Request): CompletionStage[Response] = f(request)
    }

  implicit def liftToHeaderServiceCall[Request, Response]
  (t: ((RequestHeader, Request) => Future[Response], Int))
  (implicit ec: ExecutionContext): HeaderServiceCall[Request, Response] =
    new HeaderServiceCall[Request, Response] {
      override def invokeWithHeaders(requestHeader: RequestHeader, request: Request): CompletionStage[Pair[ResponseHeader, Response]] = {
        val (f, status) = t
        f(requestHeader, request)
          .map(response => Pair.create(new ResponseHeader(status, new MessageProtocol(), HashTreePMap.empty()), response))
      }
    }

  implicit def liftToServiceServiceCall[Request, Response](f: (Request) => CompletionStage[Response]): ServerServiceCall[Request, Response] = new ServerServiceCall[Request,Response] {
    override def invoke(request: Request): CompletionStage[Response] = f(request)
  }
}
