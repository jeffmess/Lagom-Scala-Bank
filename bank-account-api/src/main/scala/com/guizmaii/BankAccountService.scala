package com.guizmaii

import com.lightbend.lagom.javadsl.api.{Descriptor, Service}
import com.lightbend.lagom.javadsl.api.ScalaService._

trait BankAccountService extends Service {
  override def descriptor(): Descriptor = {
    named("Bank-account").withAutoAcl(true)
  }
}
