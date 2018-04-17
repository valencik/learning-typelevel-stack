package ca.valencik.ltls.repository

import cats.effect.Async
import cats.syntax.applicativeError._
import ca.valencik.ltls.model._
import ca.valencik.ltls.repository.algebra.UserRepository
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.invariant.UnexpectedEnd
import doobie.util.query.Query0
import doobie.util.transactor.Transactor

// It requires a created database `users` with db user `postgres` and password `postgres`
// See `users.sql` file in resources.
class PostgresUserRepository[F[_]: Async](xa: Transactor[F])
    extends UserRepository[F] {

  override def findUser(username: UserName): F[Option[User]] = {
    val statement: ConnectionIO[UserDTO] =
      UserStatement.findUser(username).unique

    // You might have more than one query involving joins.
    // In such case a for-comprehension would be better
    val program: ConnectionIO[User] = statement.map(_.toUser)

    program.map(Option.apply).transact(xa).recoverWith {
      // In case the user is not unique in your db. Check out Doobie's docs.
      case UnexpectedEnd => Async[F].delay(None)
    }
  }

  override def findAll(): F[Option[List[User]]] = {
    val statement: ConnectionIO[List[UserDTO]] = UserStatement.findAll.to[List]

    val program: ConnectionIO[List[User]] = statement.map(_.map(_.toUser))

    program.map(Option.apply).transact(xa)
  }

}

object UserStatement {

  def findUser(username: UserName): Query0[UserDTO] = {
    sql"SELECT * FROM api_user WHERE username=${username.value}"
      .query[UserDTO]
  }

  def findAll: Query0[UserDTO] = {
    sql"SELECT * FROM api_user"
      .query[UserDTO]
  }

}
