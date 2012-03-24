package com.gu.upcoming.filters

import org.scalatra._
import com.gu.scalatra._
import com.gu.scalatra.openid._
import com.gu.upcoming.management._

class Admin extends ScalatraFilter with GoogleOpenIdConsumer with GuardianUserAuthorisation with SessionStorageStrategy {

  lazy val protectedPaths = ConfigurationManager.authenticate match {
    case false => List()
    case _ => List("/admin*", "/event*")
  }
  lazy val logoutPath = "/logout"
  lazy val logoutRedirect = "/loggedout"
  lazy val authenticationReturnPath = "/verify"
  lazy val authenticationReturnUri = ConfigurationManager.authenticationReturnUri
  lazy val secretKey = "12345678"

}