package com.ptrlab

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class PTRActor1 extends Actor with ActorLogging{

  var loop: Boolean = true;
  val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")


  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    println("That 's remote:" + remoteActor)
    self ! StartLoop()
  }
  override def receive: Receive = {
    case msg: StartLoop => {
      println("1: Subscribe to a given topic")
      println("2: Unsubscribe from given topic")
      println("3: Wait for message")
      println("Your order, sir?")
      val x : Int = StdIn.readInt()
      x match {
        case 1 =>
          println("Desired topic?: ")
          val topicName = StdIn.readLine()
          remoteActor ! Subscribe(topicName)
        case 2 =>
          println("Desired topic?: ")
          val topicName = StdIn.readLine()
          remoteActor ! Unsubscribe(topicName)
        case 3 =>
          println("Got it")
      }
    }
    case msg: String => {
      println("got message from broker: " + msg)
      self ! StartLoop()
    }
  }
}
object PTRActor1 {

  def main(args: Array[String]) {

    val config = ConfigFactory.load.getConfig("LocalActor1")
    val system = ActorSystem("ClientSystem1",config)
    val localActor = system.actorOf(Props[PTRActor1], name="local1")
  }

}