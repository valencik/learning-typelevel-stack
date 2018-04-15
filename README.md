Learning Typelevel Stack
========================

This is my toy project to learn the Typelevel stack using the excellent [Typelevel Stack Template][typelevel-stack] by [@gvolpe][gvolpe].


Libraries
---------

- [Http4s][http4s] 
- [Doobie][doobie] 
- [Circe][circe] 
- [Cats Effect][cats-effect] 
- [Fs2][fs2]
- [Cats][cats]


QuickStart
----------

1. `git clone https://github.com/valencik/learning-typelevel-stack && cd learning-typelevel-stack`
2. `brew install postgres`
3. `brew services start postgres`
4. `psql -c 'create user postgres createdb'`
5. `psql -c 'create database users' -U postgres`
6. `psql -d users -U postgres -f src/main/resources/users.sql`
7. `sbt test` (optional)
8. `sbt run`
9. `curl http://localhost:8080/users/USERNAME`


About Template
--------------

It contains the minimal code to get you started:

- `UserRepository`: Defines a method to find a user without commiting to a Monad (kind of tagless-final).
  - `PostgresUserRepository`: Implementation of the UserRepository interface using Doobie and PostgreSQL abstracting over the Effect `F[_]`.
- `UserService`: Business logic on top of the UserRepository again abstracting over the Effect `F[_]`.
- `UserHttpEndpoint`: Defines the http endpoints of the REST API making use of the UserService.
- `HttpErrorHandler`: Mapping business errors to http responses in a single place.
- `http` package: Includes custom Circe Json Encoders for value classes.
- `validation` object: Includes fields validation using `cats.data.ValidatedNel`.
- `Module`: Dependencies module.
- `Server`: The main application that wires all the components and starts the web server.


Thanks
------

Thank you to [@gvolpe][gvolpe] for creating the wonderful [typelevel stack template][typelevel-stack] and giving me a place to start with this.


License
-------
Written in <2018> by [@valencik][valencik]

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this work to the public domain worldwide.
This work is distributed without any warranty.
See <http://creativecommons.org/publicdomain/zero/1.0/>.

[typelevel-stack]: https://github.com/gvolpe/typelevel-stack.g8
[typelevel]: https://typelevel.org
[http4s]: http://http4s.org/
[doobie]: http://tpolecat.github.io/doobie/
[circe]: https://circe.github.io/circe/
[cats-effect]: https://github.com/typelevel/cats-effect
[cats]: https://typelevel.org/cats/
[fs2]: https://github.com/functional-streams-for-scala/fs2

[sbt]: http://www.scala-sbt.org/1.x/docs/Setup.html
[postgresql]: https://www.postgresql.org/download/
[gvolpe]: https://github.com/gvolpe
[valencik]: https://github.com/valencik
