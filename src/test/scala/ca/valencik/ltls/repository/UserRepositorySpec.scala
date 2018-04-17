package ca.valencik.ltls.repository

import ca.valencik.ltls.model.UserName
import cats.effect.IO
import ca.valencik.ltls.IOAssertion
import ca.valencik.ltls.TestUsers._

class UserRepositorySpec extends RepositorySpec {

  private val repo = new PostgresUserRepository[IO](transactor)

  test("find a user"){
    IOAssertion {
      for {
        user <- repo.findUser(users.head.username)
      } yield {
        user.fold(fail("User not found")) { u =>
          assert(u.username.value == users.head.username.value)
          assert(u.email.value == users.head.email.value)
        }
      }
    }
  }

  test("not find a fake user"){
    IOAssertion {
      for {
        user <- repo.findUser(UserName("fakeuser"))
      } yield {
        assert(user.isEmpty)
      }
    }
  }

  test("find all users"){
    IOAssertion {
      for {
        allUsers <- repo.findAll
      } yield {
        allUsers.fold(fail("No users")) { us =>
          assert(us.size == 2)
          assert(us.contains(users.head))
        }
      }
    }
  }

  test("check find user query") {
    check(UserStatement.findUser(users.head.username))
  }

  test("check find all users query") {
    check(UserStatement.findAll)
  }

}
