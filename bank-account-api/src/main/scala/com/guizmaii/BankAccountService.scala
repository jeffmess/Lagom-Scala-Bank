package com.guizmaii

import akka.{NotUsed, Done}
import com.lightbend.lagom.javadsl.api.ScalaService._
import com.lightbend.lagom.javadsl.api.transport.Method._
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}

object BankAccountService {

  final case class CreateAccountRequest(owner: String)

  final case class AccountDTO(id: String, owner: String, value: Double)

  final case class TransactionDTO(value: Double)

}

trait BankAccountService extends Service {

  import BankAccountService._

  def accounts(): ServiceCall[NotUsed, List[AccountDTO]]

  def account(id: String): ServiceCall[NotUsed, AccountDTO]

  def createAccount(): ServiceCall[CreateAccountRequest, AccountDTO]

  def transactions(accountId: String): ServiceCall[NotUsed, List[TransactionDTO]]

  def createTransaction(accountId: String): ServiceCall[TransactionDTO, Done]

  override def descriptor(): Descriptor = {
    named("BankAccount")
      .withCalls(
        restCall(GET,   "/api/accounts", accounts _),
        restCall(GET,   "/api/accounts/:id", account _),
        restCall(POST,  "/api/accounts", createAccount _),
        restCall(GET,   "/api/accounts/:id/transactions", transactions _),
        restCall(POST,  "/api/accounts/:id/transactions", createTransaction _)
      ).withAutoAcl(true)
  }
}
