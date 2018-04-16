package ca.valencik.ltls.http

import cats.Monad
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import ca.valencik.ltls.model.{ApiError, UserNotFound}

class HttpErrorHandler[F[_]: Monad] extends Http4sDsl[F] {

  // Map your business errors to responses here
  val handle: ApiError => F[Response[F]] = {
    case UserNotFound(u) => NotFound(s"User not found ${u.value}")
  }

}
