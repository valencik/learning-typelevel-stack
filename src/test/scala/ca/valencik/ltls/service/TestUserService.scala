package ca.valencik.ltls.service

import cats.effect.IO
import ca.valencik.ltls.TestUsers.users
import ca.valencik.ltls.model.{UserName, User}
import ca.valencik.ltls.repository.algebra.UserRepository

object TestUserService {

  class TestUserRepo extends UserRepository[IO] {
    override def findUser(username: UserName): IO[Option[User]] = IO {
      users.find(_.username.value == username.value)
    }

    override def findAll: IO[Option[List[User]]] = IO {
      Some(users)
    }

  }

  val service: UserService[IO] = new UserService[IO](new TestUserRepo)

}
