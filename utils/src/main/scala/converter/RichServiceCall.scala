package converter

import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.RequestHeader

import scala.compat.java8.OptionConverters._

object RichServiceCall {

  implicit class RichServiceCall[Request, Response](f: ServiceCall[Request, Response]) {
    def withAuthorizationHeader(requestHeader: RequestHeader): ServiceCall[Request, Response] = {
      val authorizationHeader: Option[String] = requestHeader.getHeader("Authorization").asScala

      def passHeader(requestHeader: RequestHeader): RequestHeader =
        authorizationHeader.fold(requestHeader)(token => requestHeader.withHeader("Authorization", token))

      f.handleRequestHeader(passHeader)
    }

    def invokeWithAuthorizationHeader(requestHeader: RequestHeader)(request: Request) =
      f.withAuthorizationHeader(requestHeader).invoke(request)
  }

}
