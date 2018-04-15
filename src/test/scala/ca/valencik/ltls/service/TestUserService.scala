package ca.valencik.ltls.service

import cats.effect.IO
import ca.valencik.ltls.TestUsers.users
import ca.valencik.ltls.model.UserName
import ca.valencik.ltls.repository.algebra.UserRepository

object TestUserService {

  private val testUserRepo: UserRepository[IO] =
    (username: UserName) => IO {
      users.find(_.username.value == username.value)
    }

  val service: UserService[IO] = new UserService[IO](testUserRepo)

}
