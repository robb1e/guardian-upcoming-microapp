package com.gu.upcoming.filters

import org.scalatra._
import com.gu.scalatra._
import com.gu.scalatra.openid._

class Admin extends ScalatraFilter with GoogleOpenIdConsumer with GuardianUserAuthorisation with SessionStorageStrategy {

  lazy val protectedPaths = List("/admin*")
  lazy val logoutPath = "/logout"
  lazy val logoutRedirect = "/loggedout"
  lazy val authenticationReturnPath = "/authentication/google/verification"
  lazy val authenticationReturnUri = "http://localhost:8080/authentication/google/verification"
  lazy val secretKey = "12345678"

}