package com.guizmaii

import java.time.Instant

import com.lightbend.lagom.javadsl.persistence.{AggregateEvent, AggregateEventTag}
import com.lightbend.lagom.serialization.Jsonable

object BankAccountEvent {
  val Tag = AggregateEventTag.of(classOf[BankAccountEvent])
}

sealed trait BankAccountEvent extends AggregateEvent[BankAccountEvent] with Jsonable {
  override def aggregateTag: AggregateEventTag[BankAccountEvent] = BankAccountEvent.Tag
}

final case class BankAccountCreated(
  account: Account,
  timestamp: Instant = Instant.now()
) extends BankAccountEvent
