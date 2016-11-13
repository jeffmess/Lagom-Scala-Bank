package com.guizmaii

import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport

class BankAccountServiceModule extends AbstractModule with ServiceGuiceSupport {
  override def configure(): Unit = bindServices(serviceBinding(classOf[BankAccountService], classOf[BankAccountServiceImpl]))
}
