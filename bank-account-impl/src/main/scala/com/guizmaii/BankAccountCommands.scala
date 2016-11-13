package com.guizmaii

import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import com.lightbend.lagom.serialization.Jsonable

sealed trait BankAccountCommand extends Jsonable

final case class CreateBankAccount(
  owner: String
) extends PersistentEntity.ReplyType[Account] with BankAccountCommand

final case class GetBankAccount() extends PersistentEntity.ReplyType[Account] with BankAccountCommand
