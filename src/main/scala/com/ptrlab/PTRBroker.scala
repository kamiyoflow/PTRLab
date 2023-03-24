package com.ptrlab

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable.Map


class PTRBroker extends Actor with ActorLogging {


  var msgList: Map[ActorRef, String] = Map()

  override def receive: Receive = {
    case msg: Subscribe => {
      println("remote received " + msg.topic)
      println("Subscribed!")
      msgList += (sender() -> msg.topic)
      println("Actor's list:")
      for(element<-msgList){
        println(element)
      }
      sender ! msg.topic
    }
    case msg: Unsubscribe => {
      println("remote received " + msg.topic)
      println("Unsubscribed!")
      msgList.subtractOne(sender())
      println("Actor's list:")
      for(element<-msgList){
        println(element)
      }
      sender ! "Unsubscribed from:" + msg.topic
    }

    case msg: Publish => {
      var existingTopic = false
      println("Publish message received: " + msg.message)
      if (msgList.size > 0) {
      for(element<-msgList) {
          if (msg.topic == element._2) {
            element._1 ! msg.message
            println("Published!")
            sender ! "Published successfully!"
            existingTopic = true
          }
        }
        if(existingTopic == false){
          println("No such topics!")
          sender ! "No such topics"
        }
      }
      else {
        println("No subscribers!")
        sender ! "No subscribers!"
      }
    }
    case msg: AggregatorMessage => {
      println("Publish message received: " + msg.id)
      if (msgList.size > 0) {
        for(element<-msgList) {
            element._1 ! msg
            println("Published!")
            sender ! "Published successfully!"
          }
        }
    }
    case _ => println("Received unknown msg")
  }

}
object PTRBroker {
  def main(args: Array[String]) {
    val config = ConfigFactory.load.getConfig("RemoteActor")
    val system = ActorSystem("RemoteSystem", config)
    val remote = system.actorOf(Props[PTRBroker], name = "remote")
    println("remote is ready")
  }
}