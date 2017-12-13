package chapter4

import akka.actor.ActorLogging
import akka.persistence._


object Counter {
  sealed trait Operation{
    val count: Int
  }
  case class Increment(override val count: Int) extends Operation
  case class Decrement(override val count: Int) extends Operation
  case class Cmd(op: Operation)
  case class Evt(op: Operation)
  case class State(count: Int)
}

class Counter extends PersistentActor with ActorLogging{
  import Counter._
  println("Starting .....................")
  override def persistenceId = "counter-example"

  var state: State = State(count = 0)
  def updateState(evt: Evt): Unit = evt match {
    case Evt(Increment(count)) =>
      state = State(state.count+count)
      takeSnapshot
    case Evt(Decrement(count)) =>
      state = State(state.count - count)
      takeSnapshot
  }
  override def receiveRecover = {
    case evt: Evt =>
      println(s"Counter receive ${evt} on recovering mood")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counter receive snapshot with data: ${snapshot} on recovering mood")
      state = snapshot
    case RecoveryCompleted =>
      println("Recovery Complete and Now I'll switch to receiving mode :)")
  }

  override def receiveCommand = {
    case cmd @ Cmd(op) =>
      println(s"Counter receive ${cmd}")
      persist(Evt(op)){ evt =>
        updateState(evt)
      }
    case "print" =>
      println(s"the current state of counter is ${state}")
    case SaveSnapshotSuccess(metadata) =>
      println(s"save snapshot succeed")
    case SaveSnapshotFailure(metadata,reason) =>
      println(s"save snapshot failed with reason: ${reason}")
  }


  def takeSnapshot = {
    if(state.count % 5 == 0)
      saveSnapshot(state)
  }

  //override def recovery = Recovery.none
}
