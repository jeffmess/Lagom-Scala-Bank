package fp

object RichBoolean {

  implicit class RichBoolean(val boolean: Boolean) extends AnyVal {
    def toOption: Option[_] = if (boolean) Some(boolean) else None
  }

}
