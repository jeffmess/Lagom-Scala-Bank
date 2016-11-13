package fp

import scala.language.higherKinds

object RichType {
  implicit class RichType[A](something: A) {
    def lift[M[_]](m: A => M[A]): M[A] = m(something)
  }
}
