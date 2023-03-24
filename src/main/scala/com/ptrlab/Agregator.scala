package com.ptrlab
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.io.StdIn

case class AggregatorMessage(id: Int, fruct: String)extends MySerialization

class Agregator extends Actor with ActorLogging{

  val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")
  var myMap = Map.empty[Int, List[String]]


  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    println("That 's remote:" + remoteActor)
    self ! StartLoop()
  }
  override def receive: Receive = {
    case msg: StartLoop => {
      println("1: Subscribe to a given topic")
      val x : Int = StdIn.readInt()
      x match {
        case 1 =>
          println("Desired topic?: ")
          val topicName = StdIn.readLine()
          remoteActor ! Subscribe(topicName)
      }
    }
    case msg: AggregatorMessage => {
      if(msg.fruct == "mere" || msg.fruct == "pere" || msg.fruct == "struguri") {
        if (myMap.exists(_._1 == msg.id)) {
             myMap(msg.id) = msg.fruct :: myMap(msg.id)
            }
            else {
              myMap(msg.id) = List(msg.fruct)
        }
      }
      for(element<-myMap){
        if(element._2.size == 3 && element._2.contains("struguri") && element._2.contains("pere") && element._2.contains("mere")){
          println("Element sters:")
          println(element)
          myMap.subtractOne(element._1)
        }
      }
      for(element<-myMap){
        println(element)
      }
      }
    }
}
object Agregator {

  def main(args: Array[String]) {

    val config = ConfigFactory.load.getConfig("LocalAgregator")
    val system = ActorSystem("ClientAgregator",config)
    val localActor = system.actorOf(Props[Agregator], name="agregator")
  }

}
