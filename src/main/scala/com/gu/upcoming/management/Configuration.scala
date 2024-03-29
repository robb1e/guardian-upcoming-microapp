package com.gu.upcoming.management

import com.gu.conf.ConfigurationFactory
import com.gu.management.Manifest
import com.gu.conf.mongodb._

object ConfigurationManager extends MongoDataSource with MongoConfiguration {

  lazy val config = ConfigurationFactory getConfiguration ("whats-next", "conf/whats-next")

  lazy val upcomingCollection = createCollection("upcoming")

  lazy val authenticate = config.getStringProperty("authenticate", "true").toBoolean
  lazy val authenticationReturnUri = config.getStringProperty("authenticate.returnuri", "http://localhost:8080/verify")

  lazy val cacheId = {
    val id = Manifest.asKeyValuePairs.get("Build") getOrElse System.currentTimeMillis().toString
    if (!id.contains("DEV")) id.trim
    else System.currentTimeMillis().toString
  }
}