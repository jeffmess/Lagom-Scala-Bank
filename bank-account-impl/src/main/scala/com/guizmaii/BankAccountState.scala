package com.guizmaii

import com.lightbend.lagom.serialization.Jsonable


final case class Account(
  id: String,
  owner: String,
  value: Double = 0.0
)

final case class BankAccountState(account: Option[Account]) extends Jsonable {}

object BankAccountState {
  def apply(account: Account): BankAccountState = BankAccountState(Some(account))
}
