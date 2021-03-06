package ca.valencik.ltls.http

import cats.effect.IO
import cats.syntax.apply._
import io.circe.generic.auto._
import org.http4s.{EntityEncoder, HttpService, Method, Request, Status, Uri}
import org.http4s.circe._
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpecLike, Matchers}
import ca.valencik.ltls.IOAssertion
import ca.valencik.ltls.TestUsers._
import ca.valencik.ltls.http.ResponseBodyUtils._
import ca.valencik.ltls.model.{CreateUser, UserName}
import ca.valencik.ltls.service.TestUserService

class UserHttpEndpointSpec extends UserHttpEndpointFixture with FlatSpecLike with Matchers {

  implicit val errorHandler = new HttpErrorHandler[IO]

  val httpService: HttpService[IO] = new UserHttpEndpoint[IO](TestUserService.service).service

  implicit def createUserEncoder: EntityEncoder[IO, CreateUser] = jsonEncoderOf[IO, CreateUser]

  forAll(examples) { (username, expectedStatus, expectedBody) =>
    it should s"find the user with username: ${username.value}" in IOAssertion {
      val request = Request[IO](uri = Uri(path = s"/${username.value}"))
      httpService(request).value.flatMap { task =>
        task.fold(IO(fail("Empty response")) *> IO.unit) { response =>
          IO(response.status        should be (expectedStatus)) *>
          IO(response.body.asString should be (expectedBody))
        }
      }
    }
  }

  it should "find all users" in IOAssertion {
    val allUsersString = users
      .map{u => s"""{"username":"${u.username.value}","email":"${u.email.value}"}"""}
      .mkString("[", ",", "]")
    val request = Request[IO](uri = Uri(path = "/"))
    httpService(request).value.flatMap { task =>
      task.fold(IO(fail("Empty response")) *> IO.unit) { response =>
        IO(response.status should be (Status.Ok)) *>
        IO(response.body.asString shouldBe allUsersString)
      }
    }
  }

  it should "Create a user" in IOAssertion {
    for {
      req   <- Request[IO](method = Method.POST).withBody(CreateUser("root", "root@unix.org"))
      task  <- httpService(req).value
      _     <- task.fold(IO(fail("Empty response")) *> IO.unit) {response =>
                 IO(response.status should be (Status.Created))
               }
    } yield ()
  }

}

trait UserHttpEndpointFixture extends PropertyChecks {

  private val user1 = users.head
  private val user2 = users.tail.head
  private val user3 = users.last

  val examples = Table(
    ("username", "expectedStatus", "expectedBody"),
    (user1.username, Status.Ok, s"""{"username":"${user1.username.value}","email":"${user1.email.value}"}"""),
    (user2.username, Status.Ok, s"""{"username":"${user2.username.value}","email":"${user2.email.value}"}"""),
    (user3.username, Status.Ok, s"""{"username":"${user3.username.value}","email":"${user3.email.value}"}"""),
    (UserName("xxx"), Status.NotFound, "User not found xxx")
  )
}
