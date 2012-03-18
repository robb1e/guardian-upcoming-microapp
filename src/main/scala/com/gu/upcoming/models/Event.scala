package com.gu.upcoming.models

import com.novus.salat.annotations._
import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import com.gu.mongodb.SalatTypeConversions._
import com.gu.mongodb._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.gu.upcoming.management.ConfigurationManager._
import org.joda.time.DateTime

case class Event(@Key("_id") id: String = new ObjectId().toString,
  dateCreated: DateTime = new DateTime(),
  dateUpdated: DateTime = new DateTime(),
  displayFrom: DateTime = new DateTime(),
  displayUntil: DateTime,
  description: String,
  title: String,
  eventType: String = "")

object Event {

  object Dao extends SalatDAO[Event, ObjectId](collection = upcomingCollection) with CasbahConverstionHelpers

  def all[A <: String, B <: AnyRef](query: List[(A, B)] = List()) = {
    val q = MongoDBObject(query)
    Dao.find(q).toList
  }

  def find(id: String) = Dao.findOne(Map("_id" -> id))

  def upsert(upcoming: Event) = updateAndReadBack(lookupById(upcoming), upcoming.copy(dateUpdated = new DateTime()))

  def delete(upcoming: Event) = Dao.remove(upcoming)

  private def updateAndReadBack(q: Map[String, Any], upcoming: Event) = {
    Dao.collection.findAndModify(q, DBObject(), DBObject(), false, upcoming, true, true) map { grater[Event].asObject(_) }
  }

  private def lookupById(upcoming: Event) = Map("_id" -> upcoming.id)

}