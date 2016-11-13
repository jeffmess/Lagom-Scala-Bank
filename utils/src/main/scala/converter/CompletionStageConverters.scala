package converter

import java.util.concurrent.CompletionStage

import akka.NotUsed

import scala.compat.java8.FutureConverters.{CompletionStageOps, FutureOps}
import scala.concurrent.Future
import scala.language.implicitConversions

trait CompletionStageConverters {

  implicit def asCompletionStage[A](f: Future[A]): CompletionStage[A] = f.toJava

  implicit def asFuture[A](f: CompletionStage[A]): Future[A] = f.toScala

  implicit def asUnusedCompletionStage(f: CompletionStage[_]): CompletionStage[NotUsed] = f.thenApply(_ => NotUsed)
}

object CompletionStageConverters extends CompletionStageConverters
