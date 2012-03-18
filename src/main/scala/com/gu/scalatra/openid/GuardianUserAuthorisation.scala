package com.gu.scalatra.openid

trait GuardianUserAuthorisation extends UserAuthorisation {

  def isUserAuthorised(user: User) = true // user.email.endsWith("@guardian.co.uk")

}