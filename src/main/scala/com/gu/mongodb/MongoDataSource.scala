package com.gu.mongodb

import scala.collection.JavaConversions._
import com.mongodb.casbah.{ MongoURI, MongoDB, MongoConnection }
import com.mongodb.{ ServerAddress }
import com.gu.management.Loggable
import com.gu.management.mongodb.MongoManagement

trait MongoDataSource extends MongoManagement with Loggable {

  val mongoDbUsername: String
  val mongoDbPassword: String
  val mongoDbName: String

  val mongoDbUriOption: Option[String] = None
  val mongoDbHost: Option[String]

  val (connection, db) = mongoDbUriOption match {
    case Some(uri) =>
      val mongoUri = MongoURI(uri)
      val connection: MongoConnection = MongoConnection(mongoUri)
      wireInTimingMetric(connection)
      val db: MongoDB = MongoDB(connection, mongoUri.database)
      (connection, db)
    case None =>
      val host = mongoDbHost get
      val connection = MongoConnection(host.split(",").toList.map(server => new ServerAddress(server)))
      wireInTimingMetric(connection)
      val db = connection(mongoDbName)
      (connection, db)
  }

  if (!db.authenticate(mongoDbUsername, mongoDbPassword))
    throw new Exception("Authentication failed")

  protected def createCollection(name: String) = {
    val collection = db(name)
    collection.slaveOk()
    collection
  }

}