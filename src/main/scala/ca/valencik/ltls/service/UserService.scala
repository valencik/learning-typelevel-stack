package ca.valencik.ltls.service

import cats.data.EitherT
import cats.effect.Async
import cats.syntax.functor._
import ca.valencik.ltls.model._
import ca.valencik.ltls.repository.algebra.UserRepository

class UserService[F[_]: Async](userRepo: UserRepository[F]) {

  def findUser(username: UserName): F[ApiError Either User] =
    userRepo.findUser(username) map { maybeUser =>
      maybeUser.toRight[ApiError](UserNotFound(username))
    }

  def findAll: F[ApiError Either List[User]] =
    userRepo.findAll map { maybeUsers =>
      maybeUsers.toRight[ApiError](NoUsersFound)
    }

  // TODO: To be completed by final user :)
  def addUser(user: User): F[ApiError Either Unit]    = EitherT.rightT(()).value
  def updateUser(user: User): F[ApiError Either Unit] = EitherT.rightT(()).value
  def deleteUser(username: UserName): F[ApiError Either Unit] =
    EitherT.rightT(()).value
}
