package com.guizmaii

import java.util.UUID
import javax.inject.Inject

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry
import play.api.inject.ConfigurationProvider

import scala.concurrent.ExecutionContext

object BankAccountServiceImpl {

  import BankAccountService._

  implicit class RichAccount(val account: Account) extends AnyVal {
    def toDTO: AccountDTO = AccountDTO(
      id = account.id,
      owner = account.owner,
      value = account.value
    )
  }

}

class BankAccountServiceImpl @Inject()(
  config: ConfigurationProvider,
  persistentEntities: PersistentEntityRegistry
)(implicit ex: ExecutionContext) extends BankAccountService {

  import BankAccountService._
  import BankAccountServiceImpl._
  import converter.ServiceCallConverter._

  persistentEntities.register(classOf[BankAccountEntity])

  override def accounts: ServiceCall[NotUsed, List[AccountDTO]] = ???

  override def account(id: String): ServiceCall[NotUsed, AccountDTO] = _ => bankAccountEntityRef(id)
    .ask[Account, GetBankAccount](GetBankAccount())
    .map(_.toDTO)

  override def createAccount: ServiceCall[CreateAccountRequest, AccountDTO] = { request: CreateAccountRequest =>
    bankAccountEntityRef(UUID.randomUUID().toString)
      .ask[Account, CreateBankAccount](CreateBankAccount(owner = request.owner))
      .map(_.toDTO)
  }

  override def transactions(accountId: String): ServiceCall[NotUsed, List[TransactionDTO]] = ???

  override def createTransaction(accountId: String): ServiceCall[TransactionDTO, Done] = ???


  private[this] def bankAccountEntityRef(id: String) = persistentEntities.refFor(classOf[BankAccountEntity], id)

}

