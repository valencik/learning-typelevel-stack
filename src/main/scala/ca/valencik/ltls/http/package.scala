package ca.valencik.ltls

import ca.valencik.ltls.model._
import io.circe.{Encoder, Json}

package object http {

  implicit val usernameEncoder: Encoder[UserName] = Encoder.instance { x =>
    Json.fromString(x.value)
  }

  implicit val emailEncoder: Encoder[Email] = Encoder.instance { x =>
    Json.fromString(x.value)
  }

}
