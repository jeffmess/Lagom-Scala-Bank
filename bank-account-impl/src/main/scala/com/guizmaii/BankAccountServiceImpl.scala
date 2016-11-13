package com.guizmaii

import javax.inject.Inject

import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry
import play.api.inject.ConfigurationProvider

import scala.concurrent.ExecutionContext

class BankAccountServiceImpl @Inject()(
  config: ConfigurationProvider,
  persistentEntities: PersistentEntityRegistry
)(implicit ex: ExecutionContext) extends BankAccountService {

  // Needed to convert some Scala types to Java
}

