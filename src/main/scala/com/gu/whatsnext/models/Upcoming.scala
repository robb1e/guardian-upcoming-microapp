package com.gu.whatsnext.models

import com.novus.salat.annotations._
import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import com.gu.mongodb._
import com.gu.management.Loggable
import org.joda.time.DateTime
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.gu.whatsnext.management.ConfigurationManager._

case class Upcoming(dateCreated: DateTime)

object Upcoming {
  object Dao extends SalatDAO[Upcoming, ObjectId](collection = upcomingCollection) with CasbahConverstionHelpers

  def all = {
    Dao.find(MongoDBObject())
  }

}