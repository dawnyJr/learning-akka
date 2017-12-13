package chapter4

import akka.persistence.fsm._
import akka.persistence.fsm.PersistentFSM.FSMState
import scala.reflect._

object Account {
  // Account states
  sealed trait State extends FSMState
  case object Empty extends State{
    override def identifier: String = "Empty"
  }
  case object Active extends State{
    override def identifier: String = "Active"
  }
  // Account Data
  sealed trait Data{
    val amount: Float
  }
  case object ZeroBalance extends Data{
    override val amount: Float = 0.0f
  }
  case class Balance(override val amount: Float) extends Data
  // Domain Events (Persist events)
  sealed trait DomainEvent
  case class AcceptedTransaction(amount: Float, `type`: TransactionType) extends DomainEvent
  case class RejectedTransaction(amount: Float, `type`: TransactionType, reason: String) extends DomainEvent
  // Transaction Types
  sealed trait TransactionType
  case object CR extends TransactionType
  case object DR extends TransactionType
  // Commands
  case class Operation(amount: Float, `type`: TransactionType)
}

class Account extends PersistentFSM[Account.State, Account.Data, Account.DomainEvent]{

  import Account._
  override def persistenceId: String = "account"

  override def applyEvent(domainEvent: DomainEvent, currentData: Data): Data = domainEvent match {
    case AcceptedTransaction(amount, CR) =>
      val newAmount = currentData.amount + amount
      println(s"Your new Balance is: ${newAmount}")
      Balance(newAmount)
    case AcceptedTransaction(amount, DR) =>
      val newAmount = currentData.amount - amount
      println(s"Your new Balance is: ${newAmount}")
      if(newAmount > 0) Balance(newAmount)
      else ZeroBalance
    case RejectedTransaction(_,_, reason) =>
      println(s"Rejected Transaction with reason: ${reason}")
      currentData
  }


  override def domainEventClassTag: ClassTag[DomainEvent] =  classTag[DomainEvent]

  startWith(Empty, ZeroBalance)
  when(Active){
    case Event(Operation(amount, CR),_) =>
      stay applying AcceptedTransaction(amount, CR)
    case Event(Operation(amount, DR), balance) =>
      val newBalance = balance.amount - amount
      if(newBalance > 0 )
        stay applying AcceptedTransaction(amount, DR)
      else if(newBalance == 0)
        goto(Empty) applying AcceptedTransaction(amount, DR)
      else
        stay applying RejectedTransaction(amount, DR, "balance doesn't cover this operation")
  }
  when(Empty){
    case Event(Operation(amount, CR),_) =>
      println(s"Hi its your first credit operation.")
      goto(Active) applying AcceptedTransaction(amount, CR)
    case Event(Operation(amount, DR),_) =>
      println(s"Sorry your account has zero Balance.")
      stay applying RejectedTransaction(amount, DR, "Balance is Zero")
  }
}
