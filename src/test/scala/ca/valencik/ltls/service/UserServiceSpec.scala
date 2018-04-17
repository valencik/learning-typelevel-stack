package ca.valencik.ltls.service

import cats.data.EitherT
import org.scalatest.{FlatSpecLike, Matchers}
import ca.valencik.ltls.IOAssertion
import ca.valencik.ltls.TestUsers._
import ca.valencik.ltls.model.{UserName, UserNotFound}

class UserServiceSpec extends FlatSpecLike with Matchers {

  it should "retrieve an user" in IOAssertion {
    EitherT(TestUserService.service.findUser(users.head.username)).map { user =>
      user should be (users.head)
    }.value
  }

  it should "fail retrieving an user" in IOAssertion {
    EitherT(TestUserService.service.findUser(UserName("xxx"))).leftMap { error =>
      error shouldBe a[UserNotFound]
    }.value
  }

  it should "retrieve all users" in IOAssertion {
    EitherT(TestUserService.service.findAll).map { allUsers =>
      allUsers shouldBe users
    }.value
  }

}
