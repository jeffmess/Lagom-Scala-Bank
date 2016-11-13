package com.guizmaii

import java.util.Optional
import javax.inject.Inject

import com.lightbend.lagom.javadsl.persistence.PersistentEntity

import scala.collection.JavaConverters._

class BankAccountEntity @Inject()
  extends PersistentEntity[BankAccountCommand, BankAccountEvent, BankAccountState] {


  override def initialBehavior(snapshotState: Optional[BankAccountState]): Behavior = {
    val b = newBehaviorBuilder(snapshotState.orElseGet(() => BankAccountState(None)))

    b.setCommandHandler(classOf[CreateBankAccount], (cmd: CreateBankAccount, ctx: CommandContext[Account]) => {
      state.account match {
        case Some(_) =>
          ctx.invalidCommand(s"Packet $entityId already created")
          ctx.done()
        case None =>
          val account = Account(id = entityId, owner = cmd.owner)
          val events = BankAccountCreated(account = account) :: Nil
          ctx.thenPersistAll(events.asJava, () => ctx.reply(account))
      }
    })

    b.setEventHandler(classOf[BankAccountCreated], (evt: BankAccountCreated) => BankAccountState(evt.account))

    b.setReadOnlyCommandHandler(classOf[GetBankAccount], (cmd: GetBankAccount, ctx: ReadOnlyCommandContext[Account]) =>
      state.account match {
        case None => ctx.invalidCommand(s"Account $entityId not found")
        case Some(p) => ctx.reply(p)
      }
    )

    b.build()
  }

}
