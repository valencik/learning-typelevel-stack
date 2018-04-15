package ca.valencik.ltls.repository

import ca.valencik.ltls.model.{User, UserName}

object algebra {

  trait UserRepository[F[_]] {
    def findUser(username: UserName): F[Option[User]]
  }

}
